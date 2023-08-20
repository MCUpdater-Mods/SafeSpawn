package com.mcupdater.safespawn.setup;

import com.mcupdater.safespawn.tile.TileRendererInertBeam;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.mcupdater.safespawn.setup.Registration.BEACONBLOCK_TILE;

public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        BlockEntityRenderers.register(BEACONBLOCK_TILE.get(), TileRendererInertBeam::new);
    }
}
