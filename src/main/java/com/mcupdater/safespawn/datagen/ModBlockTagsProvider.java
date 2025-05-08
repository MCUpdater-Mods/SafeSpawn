package com.mcupdater.safespawn.datagen;

import com.mcupdater.safespawn.SafeSpawn;
import com.mcupdater.safespawn.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
	public ModBlockTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(packOutput, lookupProvider, SafeSpawn.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider pProvider) {
		this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.add(Registration.BEACONBLOCK.get())
				.add(Registration.SPAWNLANTERNBLOCK.get());
	}
}
