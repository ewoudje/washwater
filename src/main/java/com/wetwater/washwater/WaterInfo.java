package com.wetwater.washwater;

import com.wetwater.washwater.state.FakeBlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class WaterInfo {
    public static short volumePerBlock = 1000;
    public static short volumePerLevel = (short) ((volumePerBlock / 8) + 1);
    public static short cutOffValue = (short) (volumePerLevel * 7);
    public static short surfaceTensionLimit = 20;
    public static int flowDivider = 8;

    public static short getWaterVolumeOfState(BlockState state) {


        FluidState fluidstate = state.getFluidState();
        if (fluidstate.isEmpty()) {
            if (state.isAir() || !state.getMaterial().isSolid())
                return 0;
            else
                return -1;
        } else return (short) (fluidstate.getAmount() * volumePerLevel);
    }

    public static FluidState getWaterState(int value) {
        if (value <= 0) return Fluids.EMPTY.defaultFluidState();
        //return Fluids.WATER.getFlowing(value / volumePerLevel + 1, false);
        //System.out.println("amogus returned water");
        //return Fluids.WATER.getFlowing(8, false);
        return Fluids.WATER.defaultFluidState();
    }

    public static float getHeight(int volume) {
        if (volume < 0) return 0;
        return ((float) volume) / volumePerBlock;
    }
}
