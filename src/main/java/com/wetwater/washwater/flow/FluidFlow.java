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
            var underVolume = region.getVolume(pos.getX(), pos.getY() - 1 , pos.getZ());
            if (pos.getY() -1 == WaterInfo.minY && underVolume == 0) {
                //Delete water
                    region.setVolume(pos, 0);
            }
            else {
                if (underVolume >= 0 && underVolume < WaterInfo.volumePerBlock) {
                    var transaction = Math.min(volume, WaterInfo.volumePerBlock - underVolume);
                    region.setVolume(pos, volume - transaction);
                    region.setVolume(pos.getX(), pos.getY() - 1, pos.getZ(), underVolume + transaction);

                    volume -= transaction;

                    if (volume > 0) {
                        //Flow downwards sideways
                        equalizeWaterDownwards(region, pos, volume);
                    }
                } else {
                    //If under is solid or filled up then flow to sides
                    equalizeWater(region, pos, volume);
                }
            }



        } else {
            WaterMod.LOGGER.warn("Ticking water with no volume");
        }
    }


    public static void equalizeWater(FluidRegion region, BlockPos owner, int volume) {
        if (volume < WaterInfo.surfaceTensionLimit) return;
        int newVolume = volume;

        for (Direction direction : PseudoRandom.getRandomDirectionArray()) {
            int x = owner.getX() + direction.getStepX();
            int y = owner.getY();
            int z = owner.getZ() + direction.getStepZ();
            int otherVolume = region.getVolume(x, y, z);

            if (otherVolume < 0) continue;

            int transfer = (newVolume - otherVolume) / WaterInfo.flowDivider;
            if (transfer > 2 || transfer < -2) {
                newVolume -= transfer;
                region.setVolume(x, y, z, otherVolume + transfer);
            }
        }

        if (newVolume != volume) region.setVolume(owner, newVolume);
    }

    public static void equalizeWaterDownwards(FluidRegion region, BlockPos owner, int volume) {
        if (volume < WaterInfo.surfaceTensionLimit) return;
        int newVolume = volume;

        for (Direction direction : PseudoRandom.getRandomDirectionArray()) {
            int x = owner.getX() + direction.getStepX();
            int y = owner.getY() - 1;
            int z = owner.getZ() + direction.getStepZ();
            int otherVolume = region.getVolume(x, y, z);

            if (otherVolume < 0) continue;

            int transfer = Math.min(newVolume, WaterInfo.volumePerBlock - otherVolume);
            if (transfer > 2 || transfer < -2) {
                newVolume -= transfer;
                region.setVolume(x, y, z, otherVolume + transfer);
            }

            if (volume == 0) break;
        }

        if (newVolume != volume) region.setVolume(owner, newVolume);
    }
}
