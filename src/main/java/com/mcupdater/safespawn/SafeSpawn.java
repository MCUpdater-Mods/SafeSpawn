package com.mcupdater.safespawn;

import com.mcupdater.safespawn.setup.ClientSetup;
import com.mcupdater.safespawn.setup.Config;
import com.mcupdater.safespawn.setup.ModSetup;
import com.mcupdater.safespawn.setup.Registration;
import com.mcupdater.safespawn.world.SpawnFortFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.PlayerRespawnLogic;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.storage.ServerLevelData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mod("safespawn")
public class SafeSpawn
{
	public static final String MODID = "safespawn";
	public static final Logger LOGGER = LogManager.getLogger();
	public final EventHandler eventHandler = new EventHandler();

	public SafeSpawn(IEventBus modEventBus, ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
		Registration.init(modEventBus);

		modEventBus.addListener(ModSetup::init);
		modEventBus.addListener(ClientSetup::init);
		NeoForge.EVENT_BUS.register(eventHandler);
	}

	public class EventHandler {

		@SubscribeEvent
		public void onSpawnGenerate(LevelEvent.CreateSpawnPosition levelEvent) {
			LOGGER.info("Generating spawn area");
			if (levelEvent.getLevel() instanceof ServerLevel level) {
				if (Config.VALID_CROPS.get().isEmpty()) {
					SafeSpawn.LOGGER.info("No crops specified in config.  Generating list of known crops.");
					List<String> cropKeys = new ArrayList<>();
					@NotNull Collection<Block> crops = BuiltInRegistries.BLOCK.stream().filter(block -> (block instanceof CropBlock || block instanceof StemBlock)).toList();
					crops.forEach(block -> {
						SafeSpawn.LOGGER.info("Adding crop: " + BuiltInRegistries.BLOCK.getKey(block));
						cropKeys.add(BuiltInRegistries.BLOCK.getKey(block).toString());
					});
					Config.VALID_CROPS.set(cropKeys);
					Config.save();
				}
				ServerLevelData worldInfo = levelEvent.getSettings();
				ServerChunkCache serverChunkCache = level.getChunkSource();
				ChunkGenerator chunkgenerator = serverChunkCache.getGenerator();
				ChunkPos chunkpos = new ChunkPos(serverChunkCache.randomState().sampler().findSpawnPosition());
				int spawnHeight = chunkgenerator.getSpawnHeight(level);
				if (spawnHeight < level.getMinBuildHeight()) {
					BlockPos blockpos = chunkpos.getWorldPosition();
					spawnHeight = level.getHeight(Heightmap.Types.WORLD_SURFACE, blockpos.getX() + 8, blockpos.getZ() + 8);
				}

				worldInfo.setSpawn(chunkpos.getWorldPosition().offset(8, spawnHeight, 8), 0.0F);
				int xOffset = 0;
				int zOffset = 0;
				int i = 0;
				int j = -1;

				BlockPos blockpos1 = null;
				for(int l = 0; l < Mth.square(11); ++l) {
					if (xOffset > -5 && xOffset <= 5 && zOffset > -5 && zOffset <= 5) {
						blockpos1 = PlayerRespawnLogic.getSpawnPosInChunk(level, new ChunkPos(chunkpos.x + xOffset, chunkpos.z + zOffset));
						if (blockpos1 != null) {
							worldInfo.setSpawn(blockpos1, 0.0F);
							break;
						}
					}

					if (xOffset == zOffset || xOffset < 0 && xOffset == -zOffset || xOffset > 0 && xOffset == 1 - zOffset) {
						int k1 = i;
						i = -j;
						j = k1;
					}

					xOffset += i;
					zOffset += j;
				}

				new SpawnFortFeature(NoneFeatureConfiguration.CODEC).place(NoneFeatureConfiguration.INSTANCE, level, chunkgenerator, level.getRandom(), worldInfo.getSpawnPos().below());
				worldInfo.setSpawn(blockpos1.above(3),0.0F);
			} else {
				LOGGER.info("Not a ServerWorld");
			}
			levelEvent.setCanceled(true);
		}

	}

}
