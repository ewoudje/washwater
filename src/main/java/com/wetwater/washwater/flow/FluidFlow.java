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

            //Flow down
            var underVolume = region.getVolume(pos.getX(), pos.getY() -1 , pos.getZ());
            if (underVolume >= 0 && underVolume < WaterInfo.volumePerBlock) {
                var transaction = Math.min(volume, WaterInfo.volumePerBlock - underVolume);
                region.setVolume(pos, volume - transaction);
                region.setVolume(pos.getX(), pos.getY() - 1, pos.getZ(), underVolume + transaction);

                volume -= transaction;
            } else {
                //If under is solid or filled up then flow to sides
                equalizeWater(region, pos, volume);
            }
        } else {
            WaterMod.LOGGER.warn("Ticking water with no volume");
        }
    }


    public static void equalizeWater(FluidRegion region, BlockPos owner, int volume) {
        if (volume < WaterInfo.surfaceTensionLimit) return;
        int newVolume = volume;

        for (Direction direction : PseudoRandom.getRandomDirectionArray()) {
            BlockPos offset = owner.relative(direction);
            int otherVolume = region.getVolume(offset);
            if (otherVolume < 0) continue;

            int transfer = (newVolume - otherVolume) / WaterInfo.flowDivider;
            if (transfer > 2 || transfer < -2) {
                newVolume -= transfer;
                region.setVolume(offset, otherVolume + transfer);
            }
        }

        if (newVolume != volume) region.setVolume(owner, newVolume);
    }
}
