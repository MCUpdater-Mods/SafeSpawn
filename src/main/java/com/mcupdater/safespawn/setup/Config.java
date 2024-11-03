package com.mcupdater.safespawn.setup;

import com.mcupdater.safespawn.SafeSpawn;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Config {
    public static final ModConfigSpec COMMON_CONFIG;

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_EFFECTS = "effects";
    public static final String CATEGORY_EFFECTS_PLAYER = "player";
    public static final String CATEGORY_EFFECTS_ANIMAL = "animal";
    public static final String CATEGORY_EFFECTS_MONSTER = "monster";
    public static final String CATEGORY_EFFECTS_CRITICAL = "monster_critical";
    public static ModConfigSpec.BooleanValue EXTEND_PATHS;
    private static ModConfigSpec.ConfigValue<String> PRIMARY_CARPET;
    private static ModConfigSpec.ConfigValue<String> SECONDARY_CARPET;
    private static ModConfigSpec.ConfigValue<String> DAIS_FOCAL;
    private static ModConfigSpec.ConfigValue<String> DAIS_FOCAL2;
    public static ModConfigSpec.BooleanValue FARM_PLOTS;
    public static ModConfigSpec.ConfigValue<List<? extends String>> VALID_CROPS;

    public static ModConfigSpec.BooleanValue EFFECT_PLAYER_ENABLED;
    public static ModConfigSpec.IntValue EFFECT_PLAYER_RANGE;
    public static ModConfigSpec.IntValue EFFECT_PLAYER_HEALTHPCT;
    private static ModConfigSpec.ConfigValue<String> EFFECT_PLAYER_PRIMARYEFFECT;
    private static ModConfigSpec.IntValue EFFECT_PLAYER_PRIMARYPOWER;
    private static ModConfigSpec.IntValue EFFECT_PLAYER_PRIMARYDURATION;
    private static ModConfigSpec.ConfigValue<String> EFFECT_PLAYER_SECONDARYEFFECT;
    private static ModConfigSpec.IntValue EFFECT_PLAYER_SECONDARYPOWER;
    private static ModConfigSpec.IntValue EFFECT_PLAYER_SECONDARYDURATION;

    public static ModConfigSpec.BooleanValue EFFECT_ANIMAL_ENABLED;
    public static ModConfigSpec.IntValue EFFECT_ANIMAL_RANGE;
    private static ModConfigSpec.ConfigValue<String> EFFECT_ANIMAL_PRIMARYEFFECT;
    private static ModConfigSpec.IntValue EFFECT_ANIMAL_PRIMARYPOWER;
    private static ModConfigSpec.IntValue EFFECT_ANIMAL_PRIMARYDURATION;
    private static ModConfigSpec.ConfigValue<String> EFFECT_ANIMAL_SECONDARYEFFECT;
    private static ModConfigSpec.IntValue EFFECT_ANIMAL_SECONDARYPOWER;
    private static ModConfigSpec.IntValue EFFECT_ANIMAL_SECONDARYDURATION;

    public static ModConfigSpec.BooleanValue EFFECT_MONSTER_ENABLED;
    public static ModConfigSpec.IntValue EFFECT_MONSTER_RANGE;
    private static ModConfigSpec.ConfigValue<String> EFFECT_MONSTER_PRIMARYEFFECT;
    private static ModConfigSpec.IntValue EFFECT_MONSTER_PRIMARYPOWER;
    private static ModConfigSpec.IntValue EFFECT_MONSTER_PRIMARYDURATION;
    private static ModConfigSpec.ConfigValue<String> EFFECT_MONSTER_SECONDARYEFFECT;
    private static ModConfigSpec.IntValue EFFECT_MONSTER_SECONDARYPOWER;
    private static ModConfigSpec.IntValue EFFECT_MONSTER_SECONDARYDURATION;

    public static ModConfigSpec.BooleanValue EFFECT_MONSTER_CRITICAL_ENABLED;
    public static ModConfigSpec.IntValue EFFECT_MONSTER_CRITICAL_RANGE;
    private static ModConfigSpec.ConfigValue<String> EFFECT_MONSTER_CRITICAL_PRIMARYEFFECT;
    private static ModConfigSpec.IntValue EFFECT_MONSTER_CRITICAL_PRIMARYPOWER;
    private static ModConfigSpec.IntValue EFFECT_MONSTER_CRITICAL_PRIMARYDURATION;
    private static ModConfigSpec.ConfigValue<String> EFFECT_MONSTER_CRITICAL_SECONDARYEFFECT;
    private static ModConfigSpec.IntValue EFFECT_MONSTER_CRITICAL_SECONDARYPOWER;
    private static ModConfigSpec.IntValue EFFECT_MONSTER_CRITICAL_SECONDARYDURATION;

    public static ModConfigSpec.BooleanValue SAFESPAWN_BARREL;

    static {
        ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        {
            EXTEND_PATHS = COMMON_BUILDER.comment("Extend stairs and create docks").define("ExtendPaths", true);
            PRIMARY_CARPET = COMMON_BUILDER.comment("Center carpet").define("PrimaryCarpet","minecraft:purple_carpet");
            SECONDARY_CARPET = COMMON_BUILDER.comment("Edge carpet").define("SecondaryCarpet", "minecraft:black_carpet");
            DAIS_FOCAL = COMMON_BUILDER.comment("Block on top of dais (add \"[property=value,...]\" to set properties)").define("DaisFocal", "safespawn:inert_beacon");
            DAIS_FOCAL2 = COMMON_BUILDER.comment("Block on top of dais Y+1").define("DaisFocal2", "minecraft:air");
            FARM_PLOTS = COMMON_BUILDER.comment("Generate farm plots").define("FarmPlots", true);
            VALID_CROPS = COMMON_BUILDER.comment("List of valid crops for farm plots").defineList("ValidCrops", new ArrayList<>(), (x) -> true);
            SAFESPAWN_BARREL = COMMON_BUILDER.comment("Replace one chest with barrel of special loot (override using data/minecraft/loot_table/chests/safespawn.json)").define("SafeSpawnBarrel", true);
        }
        COMMON_BUILDER.pop();
        COMMON_BUILDER.comment("Effects").push(CATEGORY_EFFECTS);
        {
            COMMON_BUILDER.comment("Player effect").push(CATEGORY_EFFECTS_PLAYER);
            {
                EFFECT_PLAYER_ENABLED = COMMON_BUILDER.comment("Enabled").define("enabled",true);
                EFFECT_PLAYER_RANGE = COMMON_BUILDER.comment("Effect range").defineInRange("range",4,0,16);
                EFFECT_PLAYER_HEALTHPCT = COMMON_BUILDER.comment("Max health percentage to trigger").defineInRange("health_pct", 30, 0, 100);
                EFFECT_PLAYER_PRIMARYEFFECT = COMMON_BUILDER.comment("Primary effect to apply").define("primary_effect","minecraft:regeneration");
                EFFECT_PLAYER_PRIMARYPOWER = COMMON_BUILDER.comment("Strength of the primary effect").defineInRange("primary_power", 3, 0, 10);
                EFFECT_PLAYER_PRIMARYDURATION = COMMON_BUILDER.comment("Duration of the primary effect in ticks").defineInRange("primary_duration", 40, 0, 100000);
                EFFECT_PLAYER_SECONDARYEFFECT = COMMON_BUILDER.comment("Secondary effect to apply").define("secondary_effect","");
                EFFECT_PLAYER_SECONDARYPOWER = COMMON_BUILDER.comment("Strength of the secondary effect").defineInRange("secondary_power", 0, 0, 10);
                EFFECT_PLAYER_SECONDARYDURATION = COMMON_BUILDER.comment("Duration of the secondary effect in ticks").defineInRange("secondary_duration", 0, 0, 100000);
            }
            COMMON_BUILDER.pop();
            COMMON_BUILDER.comment("Animal effect").push(CATEGORY_EFFECTS_ANIMAL);
            {
                EFFECT_ANIMAL_ENABLED = COMMON_BUILDER.comment("Enabled").define("enabled",true);
                EFFECT_ANIMAL_RANGE = COMMON_BUILDER.comment("Effect range").defineInRange("range",10,0,16);
                EFFECT_ANIMAL_PRIMARYEFFECT = COMMON_BUILDER.comment("Primary effect to apply").define("primary_effect","minecraft:regeneration");
                EFFECT_ANIMAL_PRIMARYPOWER = COMMON_BUILDER.comment("Strength of the primary effect").defineInRange("primary_ power", 0, 0, 10);
                EFFECT_ANIMAL_PRIMARYDURATION = COMMON_BUILDER.comment("Duration of the primary effect in ticks").defineInRange("primary_duration", 100, 0, 100000);
                EFFECT_ANIMAL_SECONDARYEFFECT = COMMON_BUILDER.comment("Secondary effect to apply").define("secondary_effect","");
                EFFECT_ANIMAL_SECONDARYPOWER = COMMON_BUILDER.comment("Strength of the secondary effect").defineInRange("secondary_power", 0, 0, 10);
                EFFECT_ANIMAL_SECONDARYDURATION = COMMON_BUILDER.comment("Duration of the secondary effect in ticks").defineInRange("secondary_duration", 0, 0, 100000);
            }
            COMMON_BUILDER.pop();
            COMMON_BUILDER.comment("Monster effect").push(CATEGORY_EFFECTS_MONSTER);
            {
                EFFECT_MONSTER_ENABLED = COMMON_BUILDER.comment("Enabled").define("enabled",true);
                EFFECT_MONSTER_RANGE = COMMON_BUILDER.comment("Effect range").defineInRange("range",12,0,16);
                EFFECT_MONSTER_PRIMARYEFFECT = COMMON_BUILDER.comment("Primary effect to apply").define("primary_effect","minecraft:wither");
                EFFECT_MONSTER_PRIMARYPOWER = COMMON_BUILDER.comment("Strength of the primary effect").defineInRange("primary_power", 2, 0, 10);
                EFFECT_MONSTER_PRIMARYDURATION = COMMON_BUILDER.comment("Duration of the primary effect in ticks").defineInRange("primary_duration", 200, 0, 100000);
                EFFECT_MONSTER_SECONDARYEFFECT = COMMON_BUILDER.comment("Secondary effect to apply").define("secondary_effect","minecraft:glowing");
                EFFECT_MONSTER_SECONDARYPOWER = COMMON_BUILDER.comment("Strength of the secondary effect").defineInRange("secondary_power", 0, 0, 10);
                EFFECT_MONSTER_SECONDARYDURATION = COMMON_BUILDER.comment("Duration of the secondary effect in ticks").defineInRange("secondary_duration", 1000, 0, 100000);
            }
            COMMON_BUILDER.pop();
            COMMON_BUILDER.comment("Monster critical range effect").push(CATEGORY_EFFECTS_CRITICAL);
            {
                EFFECT_MONSTER_CRITICAL_ENABLED = COMMON_BUILDER.comment("Enabled").define("enabled",true);
                EFFECT_MONSTER_CRITICAL_RANGE = COMMON_BUILDER.comment("Effect range").defineInRange("monster_critical_range",8,0,16);
                EFFECT_MONSTER_CRITICAL_PRIMARYEFFECT = COMMON_BUILDER.comment("Primary effect to apply").define("primary_effect","minecraft:slowness");
                EFFECT_MONSTER_CRITICAL_PRIMARYPOWER = COMMON_BUILDER.comment("Strength of the primary effect").defineInRange("primary_power", 5, 0, 10);
                EFFECT_MONSTER_CRITICAL_PRIMARYDURATION = COMMON_BUILDER.comment("Duration of the primary effect in ticks").defineInRange("primary_duration", 200, 0, 100000);
                EFFECT_MONSTER_CRITICAL_SECONDARYEFFECT = COMMON_BUILDER.comment("Secondary effect to apply").define("secondary_effect","");
                EFFECT_MONSTER_CRITICAL_SECONDARYPOWER = COMMON_BUILDER.comment("Strength of the secondary effect").defineInRange("secondary_power", 0, 0, 10);
                EFFECT_MONSTER_CRITICAL_SECONDARYDURATION = COMMON_BUILDER.comment("Duration of the secondary effect in ticks").defineInRange("secondary_duration", 0, 0, 100000);
            }
            COMMON_BUILDER.pop();
        }
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static BlockState getBlockState(WorldGenLevel pLevel, String resource) {
        try {
            //Registry.BLOCK is not recommended for modding, but the BlockStateParser does not use the ForgeRegistries version
            return BlockStateParser.parseForBlock(pLevel.holderLookup(Registries.BLOCK), resource, true).blockState();
        } catch (Exception e) {
            SafeSpawn.LOGGER.error(e.getMessage());
            return Blocks.AIR.defaultBlockState();
        }
    }

    public static BlockState getRandomCrop(RandomSource random, WorldGenLevel pLevel) {
        return getBlockState(pLevel, VALID_CROPS.get().get(random.nextInt(VALID_CROPS.get().size())));
    }

    public static BlockState getPrimaryCarpet(WorldGenLevel pLevel) {
        return getBlockState(pLevel, PRIMARY_CARPET.get());
    }

    public static BlockState getSecondaryCarpet(WorldGenLevel pLevel) {
        return getBlockState(pLevel, SECONDARY_CARPET.get());
    }

    public static BlockState getDaisFocal(WorldGenLevel pLevel) {
        return getBlockState(pLevel, DAIS_FOCAL.get());
    }

    public static BlockState getDaisFocal2(WorldGenLevel pLevel) {
        return getBlockState(pLevel, DAIS_FOCAL2.get());
    }

    public static Optional<Holder.Reference<MobEffect>> getMobEffect(String resource) {
        return BuiltInRegistries.MOB_EFFECT.getHolder(ResourceLocation.parse(resource));
    }

    public static MobEffectInstance getPlayerEffectPrimary() {
        if (!EFFECT_PLAYER_PRIMARYEFFECT.get().isEmpty()) {
            Holder.Reference<MobEffect> effect = getMobEffect(EFFECT_PLAYER_PRIMARYEFFECT.get()).get();
            return new MobEffectInstance(effect, EFFECT_PLAYER_PRIMARYDURATION.get(), EFFECT_PLAYER_PRIMARYPOWER.get(), true, true, false);
        } else {
            return null;
        }
    }

    public static MobEffectInstance getPlayerEffectSecondary() {
        if (!EFFECT_PLAYER_SECONDARYEFFECT.get().isEmpty()) {
            Holder.Reference<MobEffect> effect = getMobEffect(EFFECT_PLAYER_SECONDARYEFFECT.get()).get();
            return new MobEffectInstance(effect, EFFECT_PLAYER_SECONDARYDURATION.get(), EFFECT_PLAYER_SECONDARYPOWER.get(), true, true, false);
        } else {
            return null;
        }
    }

    public static MobEffectInstance getAnimalEffectPrimary() {
        if (!EFFECT_ANIMAL_PRIMARYEFFECT.get().isEmpty()) {
            Holder.Reference<MobEffect> effect = getMobEffect(EFFECT_ANIMAL_PRIMARYEFFECT.get()).get();
            return new MobEffectInstance(effect, EFFECT_ANIMAL_PRIMARYDURATION.get(), EFFECT_ANIMAL_PRIMARYPOWER.get(), true, true, false);
        } else {
            return null;
        }
    }

    public static MobEffectInstance getAnimalEffectSecondary() {
        if (!EFFECT_ANIMAL_SECONDARYEFFECT.get().isEmpty()) {
            Holder.Reference<MobEffect> effect = getMobEffect(EFFECT_ANIMAL_SECONDARYEFFECT.get()).get();
            return new MobEffectInstance(effect, EFFECT_ANIMAL_SECONDARYDURATION.get(), EFFECT_ANIMAL_SECONDARYPOWER.get(), true, true, false);
        } else {
            return null;
        }
    }

    public static MobEffectInstance getMonsterEffectPrimary() {
        if (!EFFECT_MONSTER_PRIMARYEFFECT.get().isEmpty()) {
            Holder.Reference<MobEffect> effect = getMobEffect(EFFECT_MONSTER_PRIMARYEFFECT.get()).get();
            return new MobEffectInstance(effect, EFFECT_MONSTER_PRIMARYDURATION.get(), EFFECT_MONSTER_PRIMARYPOWER.get(), true, true, false);
        } else {
            return null;
        }
    }

    public static MobEffectInstance getMonsterEffectSecondary() {
        if (!EFFECT_MONSTER_SECONDARYEFFECT.get().isEmpty()) {
            Holder.Reference<MobEffect> effect = getMobEffect(EFFECT_MONSTER_SECONDARYEFFECT.get()).get();
            return new MobEffectInstance(effect, EFFECT_MONSTER_SECONDARYDURATION.get(), EFFECT_MONSTER_SECONDARYPOWER.get(), true, true, false);
        } else {
            return null;
        }
    }

    public static MobEffectInstance getCriticalEffectPrimary() {
        if (!EFFECT_MONSTER_CRITICAL_PRIMARYEFFECT.get().isEmpty()) {
            Holder.Reference<MobEffect> effect = getMobEffect(EFFECT_MONSTER_CRITICAL_PRIMARYEFFECT.get()).get();
            return new MobEffectInstance(effect, EFFECT_MONSTER_CRITICAL_PRIMARYDURATION.get(), EFFECT_MONSTER_CRITICAL_PRIMARYPOWER.get(), true, true, false);
        } else {
            return null;
        }
    }

    public static MobEffectInstance getCriticalEffectSecondary() {
        if (!EFFECT_MONSTER_CRITICAL_SECONDARYEFFECT.get().isEmpty()) {
            Holder.Reference<MobEffect> effect = getMobEffect(EFFECT_MONSTER_CRITICAL_SECONDARYEFFECT.get()).get();
            return new MobEffectInstance(effect, EFFECT_MONSTER_CRITICAL_SECONDARYDURATION.get(), EFFECT_MONSTER_CRITICAL_SECONDARYPOWER.get(), true, true, false);
        } else {
            return null;
        }
    }
}
