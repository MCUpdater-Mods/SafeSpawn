package com.mcupdater.safespawn.setup;

import com.mcupdater.safespawn.SafeSpawn;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.command.arguments.PotionArgument;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.common.ForgeConfigSpec;

import static com.mcupdater.safespawn.setup.Registration.BEACONBLOCK;

public class Config {
    public static ForgeConfigSpec COMMON_CONFIG;

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_EFFECTS = "effects";
    public static final String CATEGORY_EFFECTS_PLAYER = "player";
    public static final String CATEGORY_EFFECTS_ANIMAL = "animal";
    public static final String CATEGORY_EFFECTS_MONSTER = "monster";
    public static final String CATEGORY_EFFECTS_CRITICAL = "monster_critical";
    public static ForgeConfigSpec.BooleanValue EXTEND_PATHS;
    private static ForgeConfigSpec.ConfigValue<String> PRIMARY_CARPET;
    private static ForgeConfigSpec.ConfigValue<String> SECONDARY_CARPET;
    private static ForgeConfigSpec.ConfigValue<String> DAIS_FOCAL;

    public static ForgeConfigSpec.BooleanValue EFFECT_PLAYER_ENABLED;
    public static ForgeConfigSpec.IntValue EFFECT_PLAYER_RANGE;
    public static ForgeConfigSpec.IntValue EFFECT_PLAYER_HEALTHPCT;
    private static ForgeConfigSpec.ConfigValue<String> EFFECT_PLAYER_PRIMARYEFFECT;
    private static ForgeConfigSpec.IntValue EFFECT_PLAYER_PRIMARYPOWER;
    private static ForgeConfigSpec.IntValue EFFECT_PLAYER_PRIMARYDURATION;
    private static ForgeConfigSpec.ConfigValue<String> EFFECT_PLAYER_SECONDARYEFFECT;
    private static ForgeConfigSpec.IntValue EFFECT_PLAYER_SECONDARYPOWER;
    private static ForgeConfigSpec.IntValue EFFECT_PLAYER_SECONDARYDURATION;

    public static ForgeConfigSpec.BooleanValue EFFECT_ANIMAL_ENABLED;
    public static ForgeConfigSpec.IntValue EFFECT_ANIMAL_RANGE;
    private static ForgeConfigSpec.ConfigValue<String> EFFECT_ANIMAL_PRIMARYEFFECT;
    private static ForgeConfigSpec.IntValue EFFECT_ANIMAL_PRIMARYPOWER;
    private static ForgeConfigSpec.IntValue EFFECT_ANIMAL_PRIMARYDURATION;
    private static ForgeConfigSpec.ConfigValue<String> EFFECT_ANIMAL_SECONDARYEFFECT;
    private static ForgeConfigSpec.IntValue EFFECT_ANIMAL_SECONDARYPOWER;
    private static ForgeConfigSpec.IntValue EFFECT_ANIMAL_SECONDARYDURATION;

    public static ForgeConfigSpec.BooleanValue EFFECT_MONSTER_ENABLED;
    public static ForgeConfigSpec.IntValue EFFECT_MONSTER_RANGE;
    private static ForgeConfigSpec.ConfigValue<String> EFFECT_MONSTER_PRIMARYEFFECT;
    private static ForgeConfigSpec.IntValue EFFECT_MONSTER_PRIMARYPOWER;
    private static ForgeConfigSpec.IntValue EFFECT_MONSTER_PRIMARYDURATION;
    private static ForgeConfigSpec.ConfigValue<String> EFFECT_MONSTER_SECONDARYEFFECT;
    private static ForgeConfigSpec.IntValue EFFECT_MONSTER_SECONDARYPOWER;
    private static ForgeConfigSpec.IntValue EFFECT_MONSTER_SECONDARYDURATION;

    public static ForgeConfigSpec.BooleanValue EFFECT_MONSTER_CRITICAL_ENABLED;
    public static ForgeConfigSpec.IntValue EFFECT_MONSTER_CRITICAL_RANGE;
    private static ForgeConfigSpec.ConfigValue<String> EFFECT_MONSTER_CRITICAL_PRIMARYEFFECT;
    private static ForgeConfigSpec.IntValue EFFECT_MONSTER_CRITICAL_PRIMARYPOWER;
    private static ForgeConfigSpec.IntValue EFFECT_MONSTER_CRITICAL_PRIMARYDURATION;
    private static ForgeConfigSpec.ConfigValue<String> EFFECT_MONSTER_CRITICAL_SECONDARYEFFECT;
    private static ForgeConfigSpec.IntValue EFFECT_MONSTER_CRITICAL_SECONDARYPOWER;
    private static ForgeConfigSpec.IntValue EFFECT_MONSTER_CRITICAL_SECONDARYDURATION;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        {
            EXTEND_PATHS = COMMON_BUILDER.comment("Extend stairs and create docks").translation("safespawn.config.extend_paths").define("ExtendPaths", true);
            PRIMARY_CARPET = COMMON_BUILDER.comment("Center carpet").translation("safespawn.config.primary_carpet").define("PrimaryCarpet","minecraft:purple_carpet");
            SECONDARY_CARPET = COMMON_BUILDER.comment("Edge carpet").translation("safespawn.config.secondary_carpet").define("SecondaryCarpet", "minecraft:black_carpet");
            DAIS_FOCAL = COMMON_BUILDER.comment("Block on top of dais").translation("safespawn.config.dais_focal").define("DaisFocal", "safespawn:inert_beacon");
        }
        COMMON_BUILDER.pop();
        COMMON_BUILDER.comment("Effects").push(CATEGORY_EFFECTS);
        {
            COMMON_BUILDER.comment("Player effect").push(CATEGORY_EFFECTS_PLAYER);
            {
                EFFECT_PLAYER_ENABLED = COMMON_BUILDER.comment("Enabled").translation("safespawn.config.enabled").define("enabled",true);
                EFFECT_PLAYER_RANGE = COMMON_BUILDER.comment("Effect range").translation("safespawn.config.range").defineInRange("range",4,0,16);
                EFFECT_PLAYER_HEALTHPCT = COMMON_BUILDER.comment("Max health percentage to trigger").translation("safespawn.config.healthpct").defineInRange("health_pct", 30, 0, 100);
                EFFECT_PLAYER_PRIMARYEFFECT = COMMON_BUILDER.comment("Primary effect to apply").translation("safespawn.config.effect").define("primary_effect","minecraft:regeneration");
                EFFECT_PLAYER_PRIMARYPOWER = COMMON_BUILDER.comment("Strength of the primary effect").translation("safespawn.config.effect").defineInRange("primary_power", 3, 0, 10);
                EFFECT_PLAYER_PRIMARYDURATION = COMMON_BUILDER.comment("Duration of the primary effect in ticks").translation("safespawn.config.duration").defineInRange("primary_duration", 40, 0, 100000);
                EFFECT_PLAYER_SECONDARYEFFECT = COMMON_BUILDER.comment("Secondary effect to apply").translation("safespawn.config.effect").define("secondary_effect","");
                EFFECT_PLAYER_SECONDARYPOWER = COMMON_BUILDER.comment("Strength of the secondary effect").translation("safespawn.config.effect").defineInRange("secondary_power", 0, 0, 10);
                EFFECT_PLAYER_SECONDARYDURATION = COMMON_BUILDER.comment("Duration of the secondary effect in ticks").translation("safespawn.config.duration").defineInRange("secondary_duration", 0, 0, 100000);
            }
            COMMON_BUILDER.pop();
            COMMON_BUILDER.comment("Animal effect").push(CATEGORY_EFFECTS_ANIMAL);
            {
                EFFECT_ANIMAL_ENABLED = COMMON_BUILDER.comment("Enabled").translation("safespawn.config.enabled").define("enabled",true);
                EFFECT_ANIMAL_RANGE = COMMON_BUILDER.comment("Effect range").translation("safespawn.config.range").defineInRange("range",10,0,16);
                EFFECT_ANIMAL_PRIMARYEFFECT = COMMON_BUILDER.comment("Primary effect to apply").translation("safespawn.config.effect").define("primary_effect","minecraft:regeneration");
                EFFECT_ANIMAL_PRIMARYPOWER = COMMON_BUILDER.comment("Strength of the primary effect").translation("safespawn.config.effect").defineInRange("primary_ power", 0, 0, 10);
                EFFECT_ANIMAL_PRIMARYDURATION = COMMON_BUILDER.comment("Duration of the primary effect in ticks").translation("safespawn.config.duration").defineInRange("primary_duration", 100, 0, 100000);
                EFFECT_ANIMAL_SECONDARYEFFECT = COMMON_BUILDER.comment("Secondary effect to apply").translation("safespawn.config.effect").define("secondary_effect","");
                EFFECT_ANIMAL_SECONDARYPOWER = COMMON_BUILDER.comment("Strength of the secondary effect").translation("safespawn.config.effect").defineInRange("secondary_power", 0, 0, 10);
                EFFECT_ANIMAL_SECONDARYDURATION = COMMON_BUILDER.comment("Duration of the secondary effect in ticks").translation("safespawn.config.duration").defineInRange("secondary_duration", 0, 0, 100000);
            }
            COMMON_BUILDER.pop();
            COMMON_BUILDER.comment("Monster effect").push(CATEGORY_EFFECTS_MONSTER);
            {
                EFFECT_MONSTER_ENABLED = COMMON_BUILDER.comment("Enabled").translation("safespawn.config.enabled").define("enabled",true);
                EFFECT_MONSTER_RANGE = COMMON_BUILDER.comment("Effect range").translation("safespawn.config.range").defineInRange("range",12,0,16);
                EFFECT_MONSTER_PRIMARYEFFECT = COMMON_BUILDER.comment("Primary effect to apply").translation("safespawn.config.effect").define("primary_effect","minecraft:wither");
                EFFECT_MONSTER_PRIMARYPOWER = COMMON_BUILDER.comment("Strength of the primary effect").translation("safespawn.config.effect").defineInRange("primary_power", 2, 0, 10);
                EFFECT_MONSTER_PRIMARYDURATION = COMMON_BUILDER.comment("Duration of the primary effect in ticks").translation("safespawn.config.duration").defineInRange("primary_duration", 200, 0, 100000);
                EFFECT_MONSTER_SECONDARYEFFECT = COMMON_BUILDER.comment("Secondary effect to apply").translation("safespawn.config.effect").define("secondary_effect","minecraft:glowing");
                EFFECT_MONSTER_SECONDARYPOWER = COMMON_BUILDER.comment("Strength of the secondary effect").translation("safespawn.config.effect").defineInRange("secondary_power", 0, 0, 10);
                EFFECT_MONSTER_SECONDARYDURATION = COMMON_BUILDER.comment("Duration of the secondary effect in ticks").translation("safespawn.config.duration").defineInRange("secondary_duration", 1000, 0, 100000);
            }
            COMMON_BUILDER.pop();
            COMMON_BUILDER.comment("Monster critical range effect").push(CATEGORY_EFFECTS_CRITICAL);
            {
                EFFECT_MONSTER_CRITICAL_ENABLED = COMMON_BUILDER.comment("Enabled").translation("safespawn.config.enabled").define("enabled",true);
                EFFECT_MONSTER_CRITICAL_RANGE = COMMON_BUILDER.comment("Effect range").translation("safespawn.config.range").defineInRange("monster_critical_range",8,0,16);
                EFFECT_MONSTER_CRITICAL_PRIMARYEFFECT = COMMON_BUILDER.comment("Primary effect to apply").translation("safespawn.config.effect").define("primary_effect","minecraft:slowness");
                EFFECT_MONSTER_CRITICAL_PRIMARYPOWER = COMMON_BUILDER.comment("Strength of the primary effect").translation("safespawn.config.effect").defineInRange("primary_power", 5, 0, 10);
                EFFECT_MONSTER_CRITICAL_PRIMARYDURATION = COMMON_BUILDER.comment("Duration of the primary effect in ticks").translation("safespawn.config.duration").defineInRange("primary_duration", 200, 0, 100000);
                EFFECT_MONSTER_CRITICAL_SECONDARYEFFECT = COMMON_BUILDER.comment("Secondary effect to apply").translation("safespawn.config.effect").define("secondary_effect","");
                EFFECT_MONSTER_CRITICAL_SECONDARYPOWER = COMMON_BUILDER.comment("Strength of the secondary effect").translation("safespawn.config.effect").defineInRange("secondary_power", 0, 0, 10);
                EFFECT_MONSTER_CRITICAL_SECONDARYDURATION = COMMON_BUILDER.comment("Duration of the secondary effect in ticks").translation("safespawn.config.duration").defineInRange("secondary_duration", 0, 0, 100000);
            }
            COMMON_BUILDER.pop();
        }
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static BlockState getPrimaryCarpet() {
        StringReader reader = new StringReader(PRIMARY_CARPET.get());
        try {
            BlockStateParser parser = new BlockStateParser(reader,false).parse(false);
            return parser.getState();
        } catch (CommandSyntaxException e) {
            SafeSpawn.LOGGER.error(e.getMessage());
            return Blocks.PURPLE_CARPET.defaultBlockState();
        }
    }

    public static BlockState getSecondaryCarpet() {
        StringReader reader = new StringReader(SECONDARY_CARPET.get());
        try {
            BlockStateParser parser = new BlockStateParser(reader,false).parse(false);
            return parser.getState();
        } catch (CommandSyntaxException e) {
            SafeSpawn.LOGGER.error(e.getMessage());
            return Blocks.BLACK_CARPET.defaultBlockState();
        }
    }

    public static BlockState getDaisFocal() {
        StringReader reader = new StringReader(DAIS_FOCAL.get());
        try {
            BlockStateParser parser = new BlockStateParser(reader,false).parse(false);
            return parser.getState();
        } catch (CommandSyntaxException e) {
            SafeSpawn.LOGGER.error(e.getMessage());
            return BEACONBLOCK.get().defaultBlockState();
        }
    }

    public static EffectInstance getPlayerEffectPrimary() {
        try {
            if (!EFFECT_PLAYER_PRIMARYEFFECT.get().isEmpty()) {
                StringReader reader = new StringReader(EFFECT_PLAYER_PRIMARYEFFECT.get());
                Effect effect = new PotionArgument().parse(reader);
                return new EffectInstance(effect, EFFECT_PLAYER_PRIMARYDURATION.get(), EFFECT_PLAYER_PRIMARYPOWER.get(), true, true, false);
            } else {
                return null;
            }
        } catch (CommandSyntaxException e) {
            SafeSpawn.LOGGER.error(e.getMessage());
            return null;
        }
    }

    public static EffectInstance getPlayerEffectSecondary() {
        try {
            if (!EFFECT_PLAYER_SECONDARYEFFECT.get().isEmpty()) {
                StringReader reader = new StringReader(EFFECT_PLAYER_SECONDARYEFFECT.get());
                Effect effect = new PotionArgument().parse(reader);
                return new EffectInstance(effect, EFFECT_PLAYER_SECONDARYDURATION.get(), EFFECT_PLAYER_SECONDARYPOWER.get(), true, true, false);
            } else {
                return null;
            }
        } catch (CommandSyntaxException e) {
            SafeSpawn.LOGGER.error(e.getMessage());
            return null;
        }
    }

    public static EffectInstance getAnimalEffectPrimary() {
        try {
            if (!EFFECT_ANIMAL_PRIMARYEFFECT.get().isEmpty()) {
                StringReader reader = new StringReader(EFFECT_ANIMAL_PRIMARYEFFECT.get());
                Effect effect = new PotionArgument().parse(reader);
                return new EffectInstance(effect, EFFECT_ANIMAL_PRIMARYDURATION.get(), EFFECT_ANIMAL_PRIMARYPOWER.get(), true, true, false);
            } else {
                return null;
            }
        } catch (CommandSyntaxException e) {
            SafeSpawn.LOGGER.error(e.getMessage());
            return null;
        }
    }

    public static EffectInstance getAnimalEffectSecondary() {
        try {
            if (!EFFECT_ANIMAL_SECONDARYEFFECT.get().isEmpty()) {
                StringReader reader = new StringReader(EFFECT_ANIMAL_SECONDARYEFFECT.get());
                Effect effect = new PotionArgument().parse(reader);
                return new EffectInstance(effect, EFFECT_ANIMAL_SECONDARYDURATION.get(), EFFECT_ANIMAL_SECONDARYPOWER.get(), true, true, false);
            } else {
                return null;
            }
        } catch (CommandSyntaxException e) {
            SafeSpawn.LOGGER.error(e.getMessage());
            return null;
        }
    }

    public static EffectInstance getMonsterEffectPrimary() {
        try {
            if (!EFFECT_MONSTER_PRIMARYEFFECT.get().isEmpty()) {
                StringReader reader = new StringReader(EFFECT_MONSTER_PRIMARYEFFECT.get());
                Effect effect = new PotionArgument().parse(reader);
                return new EffectInstance(effect, EFFECT_MONSTER_PRIMARYDURATION.get(), EFFECT_MONSTER_PRIMARYPOWER.get(), true, true, false);
            } else {
                return null;
            }
        } catch (CommandSyntaxException e) {
            SafeSpawn.LOGGER.error(e.getMessage());
            return null;
        }
    }

    public static EffectInstance getMonsterEffectSecondary() {
        try {
            if (!EFFECT_MONSTER_SECONDARYEFFECT.get().isEmpty()) {
                StringReader reader = new StringReader(EFFECT_MONSTER_SECONDARYEFFECT.get());
                Effect effect = new PotionArgument().parse(reader);
                return new EffectInstance(effect, EFFECT_MONSTER_SECONDARYDURATION.get(), EFFECT_MONSTER_SECONDARYPOWER.get(), true, true, false);
            } else {
                return null;
            }
        } catch (CommandSyntaxException e) {
            SafeSpawn.LOGGER.error(e.getMessage());
            return null;
        }
    }

    public static EffectInstance getCriticalEffectPrimary() {
        try {
            if (!EFFECT_MONSTER_CRITICAL_PRIMARYEFFECT.get().isEmpty()) {
                StringReader reader = new StringReader(EFFECT_MONSTER_CRITICAL_PRIMARYEFFECT.get());
                Effect effect = new PotionArgument().parse(reader);
                return new EffectInstance(effect, EFFECT_MONSTER_CRITICAL_PRIMARYDURATION.get(), EFFECT_MONSTER_CRITICAL_PRIMARYPOWER.get(), true, true, false);
            } else {
                return null;
            }
        } catch (CommandSyntaxException e) {
            SafeSpawn.LOGGER.error(e.getMessage());
            return null;
        }
    }

    public static EffectInstance getCriticalEffectSecondary() {
        try {
            if (!EFFECT_MONSTER_CRITICAL_SECONDARYEFFECT.get().isEmpty()) {
                StringReader reader = new StringReader(EFFECT_MONSTER_CRITICAL_SECONDARYEFFECT.get());
                Effect effect = new PotionArgument().parse(reader);
                return new EffectInstance(effect, EFFECT_MONSTER_CRITICAL_SECONDARYDURATION.get(), EFFECT_MONSTER_CRITICAL_SECONDARYPOWER.get(), true, true, false);
            } else {
                return null;
            }
        } catch (CommandSyntaxException e) {
            SafeSpawn.LOGGER.error(e.getMessage());
            return null;
        }
    }
}
