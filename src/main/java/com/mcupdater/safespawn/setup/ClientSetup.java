package com.mcupdater.safespawn.setup;

import com.mcupdater.safespawn.tile.TileRendererInertBeam;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.mcupdater.safespawn.setup.Registration.BEACONBLOCK_TILE;

public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(Registration.BEACONBLOCK.get(), RenderType.cutoutMipped());
        ClientRegistry.bindTileEntityRenderer(BEACONBLOCK_TILE.get(), TileRendererInertBeam::new);
    }
}
