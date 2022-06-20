package com.mcupdater.safespawn.tile;

import com.mcupdater.safespawn.SafeSpawn;
import com.mcupdater.safespawn.setup.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.mcupdater.safespawn.setup.Registration.SPAWNHEARTBLOCK_TILE;

public class TileSpawnHeart extends BlockEntity {

    public TileSpawnHeart(BlockPos blockPos, BlockState blockState) {
        super(SPAWNHEARTBLOCK_TILE.get(), blockPos, blockState);
    }

    public void tick() {
        AABB playerSuffocationBounds = (new AABB(this.worldPosition)).inflate(12.0D, 6.0D, 12.0D);
        List<Player> playerSuffocationList = this.level.getEntitiesOfClass(Player.class, playerSuffocationBounds);
        for (Player player : playerSuffocationList) {
            if (player.isInWall()) {
                SafeSpawn.LOGGER.info("Moving player: " + player.getName().getString());
                player.moveTo(Vec3.atCenterOf(player.blockPosition().above(3)));
            }
        }
        if (this.level.getGameTime() % 80L == 0L) {
            if (!this.level.isClientSide) {
                if (Config.EFFECT_PLAYER_ENABLED.get()) {
                    AABB playerBounds = (new AABB(this.worldPosition.above(2))).inflate(Config.EFFECT_PLAYER_RANGE.get().doubleValue(), 4.0D, Config.EFFECT_PLAYER_RANGE.get().doubleValue());
                    List<Player> playerList = this.level.getEntitiesOfClass(Player.class, playerBounds);
                    for (Player entity : playerList) {
                        if ((entity.getHealth() / entity.getMaxHealth() * 100) <= Config.EFFECT_PLAYER_HEALTHPCT.get()) {
                            MobEffectInstance effectPrimary = Config.getPlayerEffectPrimary();
                            if (effectPrimary != null) {
                                entity.addEffect(effectPrimary);
                            }
                            MobEffectInstance effectSecondary = Config.getPlayerEffectSecondary();
                            if (effectSecondary != null) {
                                entity.addEffect(effectSecondary);
                            }
                        }
                    }
                }
                if (Config.EFFECT_ANIMAL_ENABLED.get()) {
                    AABB animalBounds = (new AABB(this.worldPosition.above(2))).inflate(Config.EFFECT_ANIMAL_RANGE.get().doubleValue(), 4.0D, Config.EFFECT_ANIMAL_RANGE.get().doubleValue());
                    List<Animal> animalList = this.level.getEntitiesOfClass(Animal.class, animalBounds);
                    for (Animal entity : animalList) {
                        MobEffectInstance effectPrimary = Config.getAnimalEffectPrimary();
                        if (effectPrimary != null) {
                            entity.addEffect(effectPrimary);
                        }
                        MobEffectInstance effectSecondary = Config.getAnimalEffectSecondary();
                        if (effectSecondary != null) {
                            entity.addEffect(effectSecondary);
                        }
                    }
                }
                if (Config.EFFECT_MONSTER_ENABLED.get()) {
                    AABB hostileBounds = (new AABB(this.worldPosition.above(2))).inflate(Config.EFFECT_MONSTER_RANGE.get().doubleValue(), 4.0D, Config.EFFECT_MONSTER_RANGE.get().doubleValue());
                    List<Monster> hostileList = this.level.getEntitiesOfClass(Monster.class, hostileBounds);
                    for (Monster entity : hostileList) {
                        MobEffectInstance effectPrimary = Config.getMonsterEffectPrimary();
                        if (effectPrimary != null) {
                            entity.addEffect(effectPrimary);
                        }
                        MobEffectInstance effectSecondary = Config.getMonsterEffectSecondary();
                        if (effectSecondary != null) {
                            entity.addEffect(effectSecondary);
                        }
                    }
                }
                if (Config.EFFECT_MONSTER_CRITICAL_ENABLED.get()) {
                    AABB hostileCriticalBounds = (new AABB(this.worldPosition.above(6))).inflate(Config.EFFECT_MONSTER_CRITICAL_RANGE.get().doubleValue(), 8.0D, Config.EFFECT_MONSTER_CRITICAL_RANGE.get().doubleValue());
                    List<Mob> hostileCriticalList = this.level.getEntitiesOfClass(Mob.class, hostileCriticalBounds);
                    for (Mob entity : hostileCriticalList) {
                        if (!(entity instanceof Animal || entity instanceof AbstractVillager)) {
                            MobEffectInstance effectPrimary = Config.getCriticalEffectPrimary();
                            if (effectPrimary != null) {
                                entity.addEffect(effectPrimary);
                            }
                            MobEffectInstance effectSecondary = Config.getCriticalEffectSecondary();
                            if (effectSecondary != null) {
                                entity.addEffect(effectSecondary);
                            }
                        }
                    }
                }
            }
        }
    }
}
