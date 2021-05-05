package com.mcupdater.safespawn.tile;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.List;

import static com.mcupdater.safespawn.setup.Registration.BEACONBLOCK_TILE;

public class TileInertBeacon extends TileEntity implements ITickableTileEntity {
    private List<BeamSegment> beamSections = Lists.newArrayList();
    private List<BeamSegment> checkingBeamSections = Lists.newArrayList();
    private int lastCheckY = -1;

    public TileInertBeacon() {
        super(BEACONBLOCK_TILE.get());
    }

    @Override
    public void tick() {
        int x = this.worldPosition.getX();
        int y = this.worldPosition.getY();
        int z = this.worldPosition.getZ();
        BlockPos blockpos;
        if (this.lastCheckY < y) {
            blockpos = this.worldPosition;
            this.checkingBeamSections = Lists.newArrayList();
            this.lastCheckY = blockpos.getY() -1;
        } else {
            blockpos = new BlockPos(x, this.lastCheckY + 1, z);
        }

        BeamSegment beamSegment = this.checkingBeamSections.isEmpty() ? null : this.checkingBeamSections.get(this.checkingBeamSections.size() -1);
        int height = this.level.getHeight(Heightmap.Type.WORLD_SURFACE, x, z);

        for (int offset = 0; offset < 10 && blockpos.getY() <= height; ++offset) {
            BlockState blockState = this.level.getBlockState(blockpos);
            Block block = blockState.getBlock();
            float[] color = blockState.getBeaconColorMultiplier(this.level, blockpos, getBlockPos());
            if (color != null) {
                if (this.checkingBeamSections.size() <= 1) {
                    beamSegment = new BeamSegment(color);
                    this.checkingBeamSections.add(beamSegment);
                } else if (beamSegment != null) {
                    if (Arrays.equals(color, beamSegment.color)) {
                        beamSegment.increaseHeight();
                    } else {
                        beamSegment = new BeamSegment(new float[]{(beamSegment.color[0] + color[0]) / 2.0F, (beamSegment.color[1] + color[1]) / 2.0F, (beamSegment.color[2] + color[2]) / 2.0F});
                        this.checkingBeamSections.add(beamSegment);
                    }
                }
            } else {
                if (beamSegment == null || blockState.getLightBlock(this.level, blockpos) >= 15 && block != Blocks.BEDROCK) {
                    this.checkingBeamSections.clear();
                    this.lastCheckY = height;
                    break;
                }
                beamSegment.increaseHeight();
            }
            blockpos = blockpos.above();
            ++this.lastCheckY;
        }

        if (this.level.getGameTime() % 80L == 0L) {
            if (!this.beamSections.isEmpty()) {
                this.playSound(SoundEvents.BEACON_AMBIENT);
            }
        }

        if (this.lastCheckY >= height) {
            this.lastCheckY = -1;
            this.beamSections = this.checkingBeamSections;
        }
    }

    public void playSound(SoundEvent soundEvent) {
        this.level.playSound(null, this.worldPosition, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getBlockPos(), getBlockPos().above(256));
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
