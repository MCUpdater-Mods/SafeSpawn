package com.mcupdater.safespawn.setup;

import com.mcupdater.safespawn.tile.BlockInertBeacon;
import com.mcupdater.safespawn.tile.BlockSpawnHeart;
import com.mcupdater.safespawn.tile.TileInertBeacon;
import com.mcupdater.safespawn.tile.TileSpawnHeart;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.mcupdater.safespawn.SafeSpawn.MODID;

public class Registration {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<BlockInertBeacon> BEACONBLOCK = BLOCKS.register("inert_beacon", BlockInertBeacon::new);
    public static final RegistryObject<Item> BEACONBLOCK_ITEM = ITEMS.register("inert_beacon", () -> new BlockItem(BEACONBLOCK.get(), new Item.Properties().tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<TileInertBeacon>> BEACONBLOCK_TILE = TILES.register("inert_beacon", () -> TileEntityType.Builder.of(TileInertBeacon::new, BEACONBLOCK.get()).build(null));

    public static final RegistryObject<BlockSpawnHeart> SPAWNHEARTBLOCK = BLOCKS.register("spawn_heart", BlockSpawnHeart::new);
    public static final RegistryObject<Item> SPAWNHEARTBLOCK_ITEM = ITEMS.register("spawn_heart", () -> new BlockItem(SPAWNHEARTBLOCK.get(), new Item.Properties().tab(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<TileSpawnHeart>> SPAWNHEARTBLOCK_TILE = TILES.register("spawn_heart", () -> TileEntityType.Builder.of(TileSpawnHeart::new, SPAWNHEARTBLOCK.get()).build(null));
}
