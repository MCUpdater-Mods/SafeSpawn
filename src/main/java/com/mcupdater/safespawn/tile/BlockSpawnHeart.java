package com.mcupdater.safespawn.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;

public class BlockSpawnHeart extends BaseEntityBlock {

    public BlockSpawnHeart(){
        super(Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileSpawnHeart(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return (lvl, pos, state, tile) -> {
            if (tile instanceof TileSpawnHeart tileHeart) {
                tileHeart.tick();
            }
        };
    }
}
