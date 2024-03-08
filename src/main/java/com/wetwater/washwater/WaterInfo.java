package com.wetwater.washwater;

import com.ewoudje.lasagna.chunkstorage.ExtraStorageSectionContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class WaterInfo {
    public static short volumePerBlock = 1000;
    public static short volumePerLevel = (short) ((volumePerBlock / 8) + 1);
    public static short cutOffValue = (short) (volumePerLevel * 7);
    public static short surfaceTensionLimit = 20;

    public static short getWaterVolumeOfState(BlockState state) {
        if (state.isAir())
            return 0;

        FluidState fluidstate = state.getFluidState();
        if (fluidstate.isEmpty())
            return -1;

        return (short) (fluidstate.getAmount() * volumePerLevel);
    }

    public static FluidState getWaterState(int value) {
        //TODO if -1 we want to check state?
        if (value == 0 || value == Short.MIN_VALUE || value == -1) return Fluids.EMPTY.defaultFluidState();
        //return Fluids.WATER.getFlowing(value / volumePerLevel + 1, false);
        //System.out.println("amogus returned water");
        //return Fluids.WATER.getFlowing(8, false);
        return Fluids.WATER.defaultFluidState();
    }
    public static float getHeight(int volume) {
        if (volume == -1) return 0;
        return (float) volume / volumePerBlock;
    }
}
