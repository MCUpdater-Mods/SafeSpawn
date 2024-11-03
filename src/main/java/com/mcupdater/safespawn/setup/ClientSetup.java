package com.mcupdater.safespawn.setup;

import com.mcupdater.safespawn.block.InertBeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import static com.mcupdater.safespawn.setup.Registration.BEACONBLOCK_TILE;

public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        BlockEntityRenderers.register(BEACONBLOCK_TILE.get(), InertBeaconRenderer::new);
    }
}
