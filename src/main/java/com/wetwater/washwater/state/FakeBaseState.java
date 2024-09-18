package com.wetwater.washwater.state;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class FakeBaseState extends BlockBehaviour.BlockStateBase {
    protected FakeBaseState(Block block, ImmutableMap<Property<?>, Comparable<?>> immutableMap, MapCodec<BlockState> mapCodec) {
        super(block, immutableMap, mapCodec);
    }

    @Override
    protected BlockState asState() {
        return null;
    }
}
