package com.wetwater.washwater.flow;

import com.wetwater.washwater.WaterInfo;
import com.wetwater.washwater.WaterMod;
import net.minecraft.core.BlockPos;

public interface FluidRegion {

    default int getVolume(BlockPos pos) {
        return getVolume(pos.getX(), pos.getY(), pos.getZ());
    }

    int getVolume(int x, int y, int z);

    default boolean isAir(BlockPos pos) {
        return isAir(pos.getX(), pos.getY(), pos.getZ());
    }
    default boolean isAir(int x, int y, int z) {
        return getVolume(x, y, z) == 0;
    }

    default boolean isWater(BlockPos pos) {
        return isWater(pos.getX(), pos.getY(), pos.getZ());
    }
    default boolean isWater(int x, int y, int z) {
        return getVolume(x, y, z) > 0;
    }

    default boolean isSolid(BlockPos pos) {
        return isSolid(pos.getX(), pos.getY(), pos.getZ());
    }
    default boolean isSolid(int x, int y, int z){
        return getVolume(x, y, z) < 0;

    }

    default void setVolume(BlockPos pos, int volume) {
        if (volume > WaterInfo.volumePerBlock) {
            setVolume(pos, WaterInfo.volumePerBlock);
            WaterMod.LOGGER.warn("Tried to set water volume higher than max");
            return;
        }

        setVolume(pos.getX(), pos.getY(), pos.getZ(), volume);
    }

    void setVolume(int x, int y, int z, int volume);

}
