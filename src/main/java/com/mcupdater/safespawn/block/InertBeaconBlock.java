package com.mcupdater.safespawn.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class InertBeaconBlock extends BaseEntityBlock implements BeaconBeamBlock {
    public static final MapCodec<InertBeaconBlock> CODEC = simpleCodec(InertBeaconBlock::new);

    @Override
    protected MapCodec<InertBeaconBlock> codec() {
        return CODEC;
    }

    public InertBeaconBlock(BlockBehaviour.Properties properties){
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new InertBeaconEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return (lvl, pos, state, tile) -> {
            if (tile instanceof InertBeaconEntity tileBeacon) {
                tileBeacon.tick();
            }
        };
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.YELLOW;
    }

}
