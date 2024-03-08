package com.wetwater.washwater.flow;

import com.wetwater.washwater.WaterInfo;
import com.wetwater.washwater.WaterMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class FluidFlow {
    private static final Direction[] HORIZONTAL_DIRECTIONS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    public static void tick(FluidRegion region, BlockPos pos) {
        int volume = region.getVolume(pos);

        if (volume > 0) {
            equalizeWater(region, pos, volume);
        } else WaterMod.LOGGER.warn("Ticking water with no volume");
    }


    public static void equalizeWater(FluidRegion region, BlockPos owner, int volume) {
        if (volume < WaterInfo.surfaceTensionLimit) return;

        for (Direction direction : HORIZONTAL_DIRECTIONS) {
            BlockPos offset = owner.relative(direction);
            int otherVolume = region.getVolume(offset);

            int transfer = (otherVolume - volume) / 2;

            if (volume + 1 > otherVolume) {
                transfer = -transfer;
            }

            if (transfer > 1) {
                region.setVolume(offset, otherVolume - transfer);
                region.setVolume(owner, volume + transfer);
            }
        }
    }
}
