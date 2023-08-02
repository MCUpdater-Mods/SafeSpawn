package com.mcupdater.safespawn;

import com.mcupdater.safespawn.setup.ClientSetup;
import com.mcupdater.safespawn.setup.Config;
import com.mcupdater.safespawn.setup.ModSetup;
import com.mcupdater.safespawn.setup.Registration;
import com.mcupdater.safespawn.world.SpawnFortFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.PlayerRespawnLogic;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("safespawn")
public class SafeSpawn
{
	public static final String MODID = "safespawn";
	public static final Logger LOGGER = LogManager.getLogger();
	public EventHandler eventHandler = new EventHandler();

	public SafeSpawn() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
		Registration.init();

		FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
		MinecraftForge.EVENT_BUS.register(eventHandler);
	}

	public class EventHandler {

		@SubscribeEvent
		public void onSpawnGenerate(LevelEvent.CreateSpawnPosition levelEvent) {
			LOGGER.info("Event fired!");
			if (levelEvent.getLevel() instanceof ServerLevel) {
				ServerLevel level = (ServerLevel) levelEvent.getLevel();
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

				new SpawnFortFeature(NoneFeatureConfiguration.CODEC).place(NoneFeatureConfiguration.INSTANCE, level, chunkgenerator, level.getRandom(), new BlockPos(worldInfo.getXSpawn(), worldInfo.getYSpawn()-1, worldInfo.getZSpawn()));
				worldInfo.setSpawn(blockpos1.above(3),0.0F);
			} else {
				LOGGER.info("Not a ServerWorld");
			}
			levelEvent.setCanceled(true);
		}

	}
}
