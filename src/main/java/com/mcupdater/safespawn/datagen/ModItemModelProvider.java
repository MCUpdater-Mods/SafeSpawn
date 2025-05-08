package com.mcupdater.safespawn.datagen;

import com.mcupdater.safespawn.SafeSpawn;
import com.mcupdater.safespawn.setup.Registration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
	public ModItemModelProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
		super(packOutput, SafeSpawn.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		block((BlockItem) Registration.BEACONBLOCK_ITEM.get());
		block((BlockItem) Registration.SPAWNLANTERNBLOCK_ITEM.get());
		block((BlockItem) Registration.SPAWNHEARTBLOCK_ITEM.get());
	}

	protected ItemModelBuilder block(BlockItem blockItem) {
		return withExistingParent(BuiltInRegistries.ITEM.getKey(blockItem).getPath(),modid + ":block/" + BuiltInRegistries.BLOCK.getKey(blockItem.getBlock()).getPath());
	}
}
