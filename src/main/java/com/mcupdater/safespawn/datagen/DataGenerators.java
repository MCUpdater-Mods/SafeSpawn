package com.mcupdater.safespawn.datagen;

import com.mcupdater.safespawn.SafeSpawn;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = SafeSpawn.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator dataGenerator = event.getGenerator();
		PackOutput packOutput = dataGenerator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		dataGenerator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput, lookupProvider));
		dataGenerator.addProvider(event.includeServer(), new ModBlockStateProvider(packOutput, existingFileHelper));
		dataGenerator.addProvider(event.includeServer(), new ModBlockTagsProvider(packOutput, lookupProvider, existingFileHelper));
		dataGenerator.addProvider(event.includeServer(), new ModItemModelProvider(packOutput, existingFileHelper));
	}
}
