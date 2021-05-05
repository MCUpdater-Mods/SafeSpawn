package com.mcupdater.safespawn;

import com.mcupdater.safespawn.setup.ClientSetup;
import com.mcupdater.safespawn.setup.Config;
import com.mcupdater.safespawn.setup.ModSetup;
import com.mcupdater.safespawn.setup.Registration;
import com.mcupdater.safespawn.world.SpawnFortFeature;
import net.minecraft.block.Block;
import net.minecraft.entity.player.SpawnLocationHelper;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

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
		public void onSpawnGenerate(WorldEvent.CreateSpawnPosition worldEvent) {
			LOGGER.info("Event fired!");
			if (worldEvent.getWorld() instanceof ServerWorld) {
				ServerWorld world = (ServerWorld) worldEvent.getWorld();
				IServerWorldInfo worldInfo = worldEvent.getSettings();
				ChunkGenerator chunkgenerator = world.getChunkSource().getGenerator();
				BiomeProvider biomeprovider = chunkgenerator.getBiomeSource();
				Random random = new Random(world.getSeed());
				BlockPos blockpos = biomeprovider.findBiomeHorizontal(0, world.getSeaLevel(), 0, 256, (biome) -> {
					return biome.getMobSettings().playerSpawnFriendly();
				}, random);
				ChunkPos chunkpos = blockpos == null ? new ChunkPos(0, 0) : new ChunkPos(blockpos);
				if (blockpos == null) {
					LOGGER.warn("Unable to find spawn biome");
				}

				boolean flag = false;

				for(Block block : BlockTags.VALID_SPAWN.getValues()) {
					if (biomeprovider.getSurfaceBlocks().contains(block.defaultBlockState())) {
						flag = true;
						break;
					}
				}

				worldInfo.setSpawn(chunkpos.getWorldPosition().offset(8, chunkgenerator.getSpawnHeight(), 8), 0.0F);
				int xOffset = 0;
				int zOffset = 0;
				int i = 0;
				int j = -1;

				BlockPos blockpos1 = null;
				for(int l = 0; l < 1024; ++l) {
					if (xOffset > -16 && xOffset <= 16 && zOffset > -16 && zOffset <= 16) {
						blockpos1 = SpawnLocationHelper.getSpawnPosInChunk(world, new ChunkPos(chunkpos.x + xOffset, chunkpos.z + zOffset), flag);
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

				new SpawnFortFeature(NoFeatureConfig.CODEC).place(world, chunkgenerator, world.getRandom(), new BlockPos(worldInfo.getXSpawn(), worldInfo.getYSpawn()-1, worldInfo.getZSpawn()), NoFeatureConfig.INSTANCE);
				worldInfo.setSpawn(blockpos1.above(3),0.0F);
			} else {
				LOGGER.info("Not a ServerWorld");
			}
			worldEvent.setCanceled(true);
		}

	}
}
