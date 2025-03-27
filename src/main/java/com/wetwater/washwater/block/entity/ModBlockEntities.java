package com.wetwater.washwater.block.entity;

import com.wetwater.washwater.WaterMod;
import com.wetwater.washwater.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static BlockEntityType<CosmicMonoxideSpoutBlockEntity> COSMIC_MONOXIDE_SPOUT;
    public static BlockEntityType<FathomlessFluidChasmBlockEntity> FATHOMLESS_FLUID_CHASM;
    public static BlockEntityType<CropBlockEntity> CROPBLOCK;

    public static void registerAllBlockEntities() {
        COSMIC_MONOXIDE_SPOUT = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new ResourceLocation(WaterMod.MODID, "cosmic_monoxide_spout"),
                FabricBlockEntityTypeBuilder.create(CosmicMonoxideSpoutBlockEntity::new,
                        ModBlocks.COSMIC_MONOXIDE_SPOUT).build(null));

        FATHOMLESS_FLUID_CHASM = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new ResourceLocation(WaterMod.MODID, "fathomless_fluid_chasm"),
                FabricBlockEntityTypeBuilder.create(FathomlessFluidChasmBlockEntity::new,
                        ModBlocks.FATHOMLESS_FLUID_CHASM).build(null));

        CROPBLOCK = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new ResourceLocation(WaterMod.MODID, "cropblock"),
                FabricBlockEntityTypeBuilder.create(CropBlockEntity::new,
                        Blocks.WHEAT).build(null));

    }
}
