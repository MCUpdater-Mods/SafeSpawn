package com.mcupdater.safespawn.tile;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockSpawnHeart extends ContainerBlock {

    public BlockSpawnHeart(){
        super(Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).noDrops().isValidSpawn((p_test_1_, p_test_2_, p_test_3_, p_test_4_) -> false));
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader world) {
        return new TileSpawnHeart();
    }

    @Override
    public BlockRenderType getRenderShape(BlockState blockState) {
        return BlockRenderType.MODEL;
    }
}
