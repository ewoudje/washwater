package com.wetwater.washwater.flow;

import com.wetwater.washwater.FluidManager;
import com.wetwater.washwater.WaterInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class SpecialFlow {


    public static boolean tryPushWater(ServerLevel level, BlockPos origin, Direction direction) {
        int maxDistance = WaterInfo.maxPistonPushingDistance;
        BlockPos newPos = origin.relative(direction);
        int currentDistance = 0;
        int volumeToDisplace = FluidManager.getVolume(level, newPos);

        while (volumeToDisplace > 0 && currentDistance < maxDistance) {
            newPos = newPos.relative(direction);
            currentDistance++;
            volumeToDisplace = FluidManager.addVolumeAndReturnRemaining(level, newPos, volumeToDisplace);
        }

        return volumeToDisplace == 0;
    }

    public static boolean checkIfCanPushWater(ServerLevel level, BlockPos origin, Direction direction) {
        int maxDistance = WaterInfo.maxPistonPushingDistance;
        BlockPos newPos = origin.relative(direction);
        int currentDistance = 0;
        int volumeToDisplace = FluidManager.getVolume(level, newPos);



        while (volumeToDisplace > 0 && currentDistance < maxDistance) {
            newPos = newPos.relative(direction);
            if (FluidManager.getVolume(level, newPos) < 0) {
                return false;
            }
            currentDistance++;
            volumeToDisplace = FluidManager.addVolumeAndReturnRemainingImaginary(level, newPos, volumeToDisplace);
        }

        return volumeToDisplace == 0;
    }
}
