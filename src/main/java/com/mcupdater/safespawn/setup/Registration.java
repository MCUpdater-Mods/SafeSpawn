package com.mcupdater.safespawn.setup;

import com.mcupdater.safespawn.block.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.mcupdater.safespawn.SafeSpawn.MODID;

public class Registration {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID);
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, MODID);

    public static void init(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        TILES.register(eventBus);
        TABS.register(eventBus);
    }

    public static final DeferredBlock<InertBeaconBlock> BEACONBLOCK = BLOCKS.register(
            "inert_beacon",
            () -> new InertBeaconBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(3.0F)
                    .lightLevel(blockState -> 15)
                    .noOcclusion()
                    .isRedstoneConductor((blockState, blockGetter, blockPos) -> false)
    ));
    public static final DeferredItem<Item> BEACONBLOCK_ITEM = ITEMS.register("inert_beacon", () -> new BlockItem(BEACONBLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InertBeaconEntity>> BEACONBLOCK_TILE = TILES.register("inert_beacon", () -> BlockEntityType.Builder.of(InertBeaconEntity::new, BEACONBLOCK.get()).build(null));

    public static final DeferredBlock<SpawnHeartBlock> SPAWNHEARTBLOCK = BLOCKS.register(
            "spawn_heart",
            () -> new SpawnHeartBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 3600000.0F)
                    .lightLevel(blockState -> 5)
                    .isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false)
    ));
    public static final DeferredItem<Item> SPAWNHEARTBLOCK_ITEM = ITEMS.register("spawn_heart", () -> new BlockItem(SPAWNHEARTBLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpawnHeartEntity>> SPAWNHEARTBLOCK_TILE = TILES.register("spawn_heart", () -> BlockEntityType.Builder.of(SpawnHeartEntity::new, SPAWNHEARTBLOCK.get()).build(null));

    public static final DeferredBlock<SpawnLanternBlock> SPAWNLANTERNBLOCK = BLOCKS.register(
            "spawn_lantern",
            () -> new SpawnLanternBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(3.0F)
                    .lightLevel(blockState -> 15)
                    .isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false)
    ));
    public static final DeferredItem<Item> SPAWNLANTERNBLOCK_ITEM = ITEMS.register("spawn_lantern", () -> new BlockItem(SPAWNLANTERNBLOCK.get(), new Item.Properties()));

    public static final Supplier<CreativeModeTab> ITEM_GROUP = TABS.register(MODID, () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + MODID))
            .icon(() -> new ItemStack(SPAWNHEARTBLOCK_ITEM.get()))
            .displayItems((params, output) -> {
                output.accept(BEACONBLOCK.get());
                output.accept(SPAWNHEARTBLOCK.get());
                output.accept(SPAWNLANTERNBLOCK.get());
            })
            .build()
    );

    public static final ResourceKey<LootTable> SAFESPAWN_LOOT = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace("chests/safespawn"));
}
