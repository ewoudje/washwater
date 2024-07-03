package com.wetwater.washwater.block;

import com.wetwater.washwater.WaterMod;
import com.wetwater.washwater.block.custom.CosmicMonoxideSpoutBlock;
import com.wetwater.washwater.block.custom.FathomlessFluidChasmBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class ModBlocks {

    public static final Block COSMIC_MONOXIDE_SPOUT = registerBlock("cosmic_monoxide_spout",
            new CosmicMonoxideSpoutBlock(FabricBlockSettings.of(Material.AMETHYST).luminance(10).sounds(SoundType.AMETHYST)), CreativeModeTab.TAB_REDSTONE);

    public static final Block FATHOMLESS_FLUID_CHASM = registerBlock("fathomless_fluid_chasm",
            new FathomlessFluidChasmBlock(FabricBlockSettings.of(Material.AMETHYST).luminance(10).sounds(SoundType.AMETHYST)), CreativeModeTab.TAB_REDSTONE);

    private static Block registerBlock(String name, Block block, CreativeModeTab group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new ResourceLocation(WaterMod.MODID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, CreativeModeTab group) {
        return Registry.register(Registry.ITEM, new ResourceLocation(WaterMod.MODID, name),
                new BlockItem(block, new FabricItemSettings().group(group)));
    }

    public static void RegisterModBlocks() {
        WaterMod.LOGGER.info("Registering ModBlocks for " + WaterMod.MODID);
    }

}
