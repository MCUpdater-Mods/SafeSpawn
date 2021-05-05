package com.mcupdater.safespawn.tile;

import com.mcupdater.safespawn.SafeSpawn;
import com.mcupdater.safespawn.setup.Config;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

import static com.mcupdater.safespawn.setup.Registration.SPAWNHEARTBLOCK_TILE;

public class TileSpawnHeart extends TileEntity implements ITickableTileEntity {

    public TileSpawnHeart() {
        super(SPAWNHEARTBLOCK_TILE.get());
    }

    @Override
    public void tick() {
        AxisAlignedBB playerSuffocationBounds = (new AxisAlignedBB(this.worldPosition)).inflate(12.0D, 6.0D, 12.0D);
        List<PlayerEntity> playerSuffocationList = this.level.getEntitiesOfClass(PlayerEntity.class, playerSuffocationBounds);
        for (PlayerEntity player : playerSuffocationList) {
            if (player.isInWall()) {
                SafeSpawn.LOGGER.info("Moving player: " + player.getName().getString());
                player.moveTo(Vector3d.atCenterOf(player.blockPosition().above(3)));
            }
        }
        if (this.level.getGameTime() % 80L == 0L) {
            if (!this.level.isClientSide) {
                if (Config.EFFECT_PLAYER_ENABLED.get()) {
                    AxisAlignedBB playerBounds = (new AxisAlignedBB(this.worldPosition.above(2))).inflate(Config.EFFECT_PLAYER_RANGE.get().doubleValue(), 4.0D, Config.EFFECT_PLAYER_RANGE.get().doubleValue());
                    List<PlayerEntity> playerList = this.level.getEntitiesOfClass(PlayerEntity.class, playerBounds);
                    for (PlayerEntity entity : playerList) {
                        if ((entity.getHealth() / entity.getMaxHealth() * 100) <= Config.EFFECT_PLAYER_HEALTHPCT.get()) {
                            EffectInstance effectPrimary = Config.getPlayerEffectPrimary();
                            if (effectPrimary != null) {
                                entity.addEffect(effectPrimary);
                            }
                            EffectInstance effectSecondary = Config.getPlayerEffectSecondary();
                            if (effectSecondary != null) {
                                entity.addEffect(effectSecondary);
                            }
                        }
                    }
                }
                if (Config.EFFECT_ANIMAL_ENABLED.get()) {
                    AxisAlignedBB animalBounds = (new AxisAlignedBB(this.worldPosition.above(2))).inflate(Config.EFFECT_ANIMAL_RANGE.get().doubleValue(), 4.0D, Config.EFFECT_ANIMAL_RANGE.get().doubleValue());
                    List<AnimalEntity> animalList = this.level.getEntitiesOfClass(AnimalEntity.class, animalBounds);
                    for (AnimalEntity entity : animalList) {
                        EffectInstance effectPrimary = Config.getAnimalEffectPrimary();
                        if (effectPrimary != null) {
                            entity.addEffect(effectPrimary);
                        }
                        EffectInstance effectSecondary = Config.getAnimalEffectSecondary();
                        if (effectSecondary != null) {
                            entity.addEffect(effectSecondary);
                        }
                    }
                }
                if (Config.EFFECT_MONSTER_ENABLED.get()) {
                    AxisAlignedBB hostileBounds = (new AxisAlignedBB(this.worldPosition.above(2))).inflate(Config.EFFECT_MONSTER_RANGE.get().doubleValue(), 4.0D, Config.EFFECT_MONSTER_RANGE.get().doubleValue());
                    List<MonsterEntity> hostileList = this.level.getEntitiesOfClass(MonsterEntity.class, hostileBounds);
                    for (MonsterEntity entity : hostileList) {
                        EffectInstance effectPrimary = Config.getMonsterEffectPrimary();
                        if (effectPrimary != null) {
                            entity.addEffect(effectPrimary);
                        }
                        EffectInstance effectSecondary = Config.getMonsterEffectSecondary();
                        if (effectSecondary != null) {
                            entity.addEffect(effectSecondary);
                        }
                    }
                }
                if (Config.EFFECT_MONSTER_CRITICAL_ENABLED.get()) {
                    AxisAlignedBB hostileCriticalBounds = (new AxisAlignedBB(this.worldPosition.above(6))).inflate(Config.EFFECT_MONSTER_CRITICAL_RANGE.get().doubleValue(), 8.0D, Config.EFFECT_MONSTER_CRITICAL_RANGE.get().doubleValue());
                    List<MobEntity> hostileCriticalList = this.level.getEntitiesOfClass(MobEntity.class, hostileCriticalBounds);
                    for (MobEntity entity : hostileCriticalList) {
                        if (!(entity instanceof AnimalEntity || entity instanceof AbstractVillagerEntity)) {
                            EffectInstance effectPrimary = Config.getCriticalEffectPrimary();
                            if (effectPrimary != null) {
                                entity.addEffect(effectPrimary);
                            }
                            EffectInstance effectSecondary = Config.getCriticalEffectSecondary();
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
