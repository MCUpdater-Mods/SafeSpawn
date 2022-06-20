package com.mcupdater.safespawn.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import javax.annotation.Nullable;

public class BlockInertBeacon extends BaseEntityBlock implements BeaconBeamBlock {

    public BlockInertBeacon(){
        super(Properties.of(Material.GLASS, MaterialColor.GOLD).strength(3.0F).lightLevel((blockState)->15).noOcclusion().isRedstoneConductor((blockState, blockReader, blockPos) -> false));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileInertBeacon(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return (lvl, pos, state, tile) -> {
            if (tile instanceof TileInertBeacon tileBeacon) {
                tileBeacon.tick();
            }
        };
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.YELLOW;
    }

}
