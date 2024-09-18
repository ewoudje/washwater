package com.wetwater.washwater.state;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class FakeBlockState extends BlockState {

    public FakeBlockState(Block block, ImmutableMap<Property<?>, Comparable<?>> immutableMap, MapCodec<BlockState> mapCodec) {
        super(block, immutableMap, mapCodec);
    }

    public FluidState notYetFakeState = Fluids.WATER.defaultFluidState();


    @Override
    public FluidState getFluidState() {
        FluidState notYetFakeState = Fluids.WATER.defaultFluidState();
        return notYetFakeState;
    }
}
