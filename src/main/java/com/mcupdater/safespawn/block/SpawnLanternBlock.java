package com.mcupdater.safespawn.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;

public class SpawnLanternBlock extends Block {
	public static final MapCodec<SpawnLanternBlock> CODEC = simpleCodec(SpawnLanternBlock::new);

	@Override
	public MapCodec<SpawnLanternBlock> codec() {
		return CODEC;
	}

	public SpawnLanternBlock(Properties properties) {
		super(properties);
	}
}
