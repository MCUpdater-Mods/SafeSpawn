package com.mcupdater.safespawn.block;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.List;

import static com.mcupdater.safespawn.setup.Registration.BEACONBLOCK_TILE;

public class InertBeaconEntity extends BlockEntity {
    private List<BeaconBeamSection> beamSections = Lists.newArrayList();
    private List<BeaconBeamSection> checkingBeamSections = Lists.newArrayList();
    private int lastCheckY = -1;

    public InertBeaconEntity(BlockPos blockPos, BlockState blockState) {
        super(BEACONBLOCK_TILE.get(), blockPos, blockState);
    }

    public void tick() {
        int x = this.worldPosition.getX();
        int y = this.worldPosition.getY();
        int z = this.worldPosition.getZ();
        BlockPos blockpos;
        if (this.lastCheckY < y) {
            blockpos = this.worldPosition;
            this.checkingBeamSections = Lists.newArrayList();
            this.lastCheckY = this.worldPosition.getY() - 1;
        } else {
            blockpos = new BlockPos(x, this.lastCheckY + 1, z);
        }

        BeaconBeamSection beamSection = this.checkingBeamSections.isEmpty() ? null : this.checkingBeamSections.get(this.checkingBeamSections.size() - 1);
        int height = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);

        for(int offset = 0; offset < 10 && blockpos.getY() <= height; ++offset) {
            BlockState blockstate = level.getBlockState(blockpos);
            Block block = blockstate.getBlock();
            Integer colorMultiplier = blockstate.getBeaconColorMultiplier(level, blockpos, this.worldPosition);
            if (colorMultiplier != null) {
                if (this.checkingBeamSections.size() <= 1) {
                    beamSection = new BeaconBeamSection(colorMultiplier);
                    this.checkingBeamSections.add(beamSection);
                } else if (beamSection != null) {
                    if (colorMultiplier == beamSection.color) {
                        beamSection.increaseHeight();
                    } else {
                        beamSection = new BeaconBeamSection(FastColor.ARGB32.average(beamSection.color, colorMultiplier));
                        this.checkingBeamSections.add(beamSection);
                    }
                }
            } else {
                if (beamSection == null || blockstate.getLightBlock(level, blockpos) >= 15 && !blockstate.is(Blocks.BEDROCK)) {
                    this.checkingBeamSections.clear();
                    this.lastCheckY = height;
                    break;
                }

                beamSection.increaseHeight();
            }

            blockpos = blockpos.above();
            ++this.lastCheckY;
        }

        if (level.getGameTime() % 80L == 0L) {
            if (!this.beamSections.isEmpty()) {
                playSound(level, this.worldPosition, SoundEvents.BEACON_AMBIENT);
            }
        }

        if (this.lastCheckY >= height) {
            this.lastCheckY = level.getMinBuildHeight() - 1;
            this.beamSections = this.checkingBeamSections;
        }

    }

    public static void playSound(Level level, BlockPos blockPos, SoundEvent soundEvent) {
        level.playSound(null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public List<BeaconBeamSection> getBeamSections() {
        return this.beamSections;
    }



    public static class BeaconBeamSection {
        final int color;
        private int height;

        public BeaconBeamSection(Integer newColor) {
            this.color = newColor;
            this.height = 1;
        }

        protected void increaseHeight() {
            ++this.height;
        }

        public int getColor() {
            return this.color;
        }

        public int getHeight() {
            return this.height;
        }
    }
}
