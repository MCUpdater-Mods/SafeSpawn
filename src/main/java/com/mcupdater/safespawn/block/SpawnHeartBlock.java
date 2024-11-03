package com.mcupdater.safespawn.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class SpawnHeartBlock extends BaseEntityBlock {
    public static final MapCodec<SpawnHeartBlock> CODEC = simpleCodec(SpawnHeartBlock::new);

    @Override
    protected MapCodec<SpawnHeartBlock> codec() {
        return CODEC;
    }

    public SpawnHeartBlock(BlockBehaviour.Properties properties){
        super(properties);
        //super(Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SpawnHeartEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return (lvl, pos, state, tile) -> {
            if (tile instanceof SpawnHeartEntity tileHeart) {
                tileHeart.tick();
            }
        };
    }
}
