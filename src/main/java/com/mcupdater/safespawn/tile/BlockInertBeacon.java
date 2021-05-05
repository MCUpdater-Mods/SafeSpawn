package com.mcupdater.safespawn.tile;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.IBeaconBeamColorProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockInertBeacon extends ContainerBlock implements IBeaconBeamColorProvider {

    public BlockInertBeacon(){
        super(Properties.of(Material.GLASS, MaterialColor.GOLD).strength(3.0F).lightLevel((blockState)->15).noOcclusion().isRedstoneConductor((blockState, blockReader, blockPos) -> false));
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader world) {
        return new TileInertBeacon();
    }

    @Override
    public BlockRenderType getRenderShape(BlockState blockState) {
        return BlockRenderType.MODEL;
    }


    @Override
    public DyeColor getColor() {
        return DyeColor.YELLOW;
    }

}
