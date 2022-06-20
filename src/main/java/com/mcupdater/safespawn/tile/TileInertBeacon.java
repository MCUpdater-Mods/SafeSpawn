package com.mcupdater.safespawn.tile;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.List;

import static com.mcupdater.safespawn.setup.Registration.BEACONBLOCK_TILE;

public class TileInertBeacon extends BlockEntity {
    private List<BeamSegment> beamSections = Lists.newArrayList();
    private List<BeamSegment> checkingBeamSections = Lists.newArrayList();
    private int lastCheckY = -1;

    public TileInertBeacon(BlockPos blockPos, BlockState blockState) {
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

        BeamSegment beamSegment = this.checkingBeamSections.isEmpty() ? null : this.checkingBeamSections.get(this.checkingBeamSections.size() - 1);
        int height = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);

        for(int offset = 0; offset < 10 && blockpos.getY() <= height; ++offset) {
            BlockState blockstate = level.getBlockState(blockpos);
            Block block = blockstate.getBlock();
            float[] colorMultiplier = blockstate.getBeaconColorMultiplier(level, blockpos, this.worldPosition);
            if (colorMultiplier != null) {
                if (this.checkingBeamSections.size() <= 1) {
                    beamSegment = new BeamSegment(colorMultiplier);
                    this.checkingBeamSections.add(beamSegment);
                } else if (beamSegment != null) {
                    if (Arrays.equals(colorMultiplier, beamSegment.color)) {
                        beamSegment.increaseHeight();
                    } else {
                        beamSegment = new BeamSegment(new float[]{(beamSegment.color[0] + colorMultiplier[0]) / 2.0F, (beamSegment.color[1] + colorMultiplier[1]) / 2.0F, (beamSegment.color[2] + colorMultiplier[2]) / 2.0F});
                        this.checkingBeamSections.add(beamSegment);
                    }
                }
            } else {
                if (beamSegment == null || blockstate.getLightBlock(level, blockpos) >= 15 && !blockstate.is(Blocks.BEDROCK)) {
                    this.checkingBeamSections.clear();
                    this.lastCheckY = height;
                    break;
                }

                beamSegment.increaseHeight();
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

    @OnlyIn(Dist.CLIENT)
    public List<BeamSegment> getBeamSections() {
        return this.beamSections;
    }

    @OnlyIn(Dist.CLIENT)
    public double getViewDistance() {
        return 256.0D;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(getBlockPos(), getBlockPos().above(256));
    }

    public static class BeamSegment {
        private final float[] color;
        private int height;

        public BeamSegment(float[] newColor) {
            this.color = newColor;
            this.height = 1;
        }

        protected void increaseHeight() {
            ++this.height;
        }

        @OnlyIn(Dist.CLIENT)
        public float[] getColor() {
            return this.color;
        }

        @OnlyIn(Dist.CLIENT)
        public int getHeight() {
            return this.height;
        }
    }
}
