package com.mcupdater.safespawn.setup;

import com.mcupdater.safespawn.tile.TileRendererInertBeam;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.mcupdater.safespawn.setup.Registration.BEACONBLOCK_TILE;

public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        //ItemBlockRenderTypes.setRenderLayer(Registration.BEACONBLOCK.get(), RenderType.cutoutMipped());
        BlockEntityRenderers.register(BEACONBLOCK_TILE.get(), TileRendererInertBeam::new);
    }
}
