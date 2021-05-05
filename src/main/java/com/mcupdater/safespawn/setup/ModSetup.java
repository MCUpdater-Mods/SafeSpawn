package com.mcupdater.safespawn.setup;

import com.mcupdater.safespawn.SafeSpawn;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = SafeSpawn.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static final ItemGroup ITEM_GROUP = new ItemGroup("safespawn") {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Registration.SPAWNHEARTBLOCK.get());
        }
    };

    public static void init(final FMLCommonSetupEvent event) {

    }
}
