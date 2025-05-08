package com.mcupdater.safespawn.datagen;

import com.mcupdater.safespawn.SafeSpawn;
import com.mcupdater.safespawn.setup.Registration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
	private final ExistingFileHelper existingFileHelper;

	public ModBlockStateProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
		super(packOutput, SafeSpawn.MODID, existingFileHelper);
		this.existingFileHelper = existingFileHelper;
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlock(Registration.BEACONBLOCK.get(), models().getBuilder(BuiltInRegistries.BLOCK.getKey(Registration.BEACONBLOCK.get()).getPath()));
		simpleBlock(Registration.SPAWNHEARTBLOCK.get(), models().getBuilder(BuiltInRegistries.BLOCK.getKey(Registration.SPAWNHEARTBLOCK.get()).getPath()));
		simpleBlock(Registration.SPAWNLANTERNBLOCK.get(), models().getBuilder(BuiltInRegistries.BLOCK.getKey(Registration.SPAWNLANTERNBLOCK.get()).getPath()));
	}
}
