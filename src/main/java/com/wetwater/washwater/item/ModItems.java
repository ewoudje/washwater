package com.wetwater.washwater.item;

import com.wetwater.washwater.WaterMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModItems {

    public static final Item FLUID_PIPETTE = registerItem("fluid_pipette", new FluidPipetteItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final Item PRECISION_BUCKET = registerItem("precision_bucket", new PrecisionBucketItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new ResourceLocation(WaterMod.MODID, name), item);

    }

    public static void RegisterModItems() {
        WaterMod.LOGGER.info("Registering ModItems for " + WaterMod.MODID);
    }

}
