package com.wetwater.washwater;

import com.ewoudje.lasagna.chunkstorage.ExtraStorageSectionContainer;
import com.wetwater.washwater.scheduling.FluidTicker;
import com.wetwater.washwater.util.DirectionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;

public class FluidManager {

    public static int tickSpeed(ServerLevel level) {
        return 2;
    }

    public static void addVolume(ServerLevel level, BlockPos pos, int volume) {
        int oldVolume = getVolume(level, pos);
        if (oldVolume < 0) {
            WaterMod.LOGGER.warn("Tried to add water volume to a non-air block");
            return;
        }

        int newVolume = oldVolume + volume;
        if (newVolume > WaterInfo.volumePerBlock) {
            setVolume(level, pos, WaterInfo.volumePerBlock);
            addVolume(level, pos.above(), newVolume - WaterInfo.volumePerBlock);
        } else {
            setVolume(level, pos, newVolume);
        }
    }

    public static int getVolume(Level level, BlockPos pos) {
        return getVolume(level, pos.getX(), pos.getY(), pos.getZ());
    }

    public static void setVolume(ServerLevel level, BlockPos pos, int volume) {
        setVolume(level, pos.getX(), pos.getY(), pos.getZ(), volume);
    }

    public static void setVolume(ServerLevel level, int x, int y, int z, int volume) {
        var chunk = level.getChunk(x >> 4, z >> 4);
        var section = chunk.getSections()[level.getSectionIndex(y)];

        FluidSection fSection = (FluidSection) ((ExtraStorageSectionContainer) section).getSectionStorage(FluidSection.ID);

        if (fSection == null) {
            fSection = new FluidSection(chunk, level.getSectionIndex(y));
            ((ExtraStorageSectionContainer) section).setSectionStorage(FluidSection.ID, fSection);
        }

        fSection.setWaterVolume(x & 15, y & 15, z & 15, (short) volume);

        if (volume != 0) {
            FluidTicker.tickWater(level, x, y, z);
        }

        for (var direction : DirectionUtils.HORIZONTAL_TOP) {
            FluidTicker.tickIfWater(level, x + direction.getStepX(), y + direction.getStepY(), z + direction.getStepZ());
        }
    }

    public static int getVolume(Level level, BlockState state, int x, int y, int z) {
        return getVolume(
                level.getChunk(x >> 4, z >> 4).getSections()[level.getSectionIndex(y)],
                state,
                x & 15, y & 15, z & 15
        );
    }

    public static int getVolume(Level level, int x, int y, int z) {
       return getVolume(
               level.getChunk(x >> 4, z >> 4).getSections()[level.getSectionIndex(y)],
               x & 15, y & 15, z & 15
       );
    }

    public static int getVolume(LevelChunkSection section, int x, int y, int z) {
        return getVolume(
                section,
                section.getBlockState(x & 15, y & 15, z & 15),
                x & 15, y & 15, z & 15
        );
    }

    public static int getVolume(LevelChunkSection section, BlockState state, int x, int y, int z) {
        var volume = WaterInfo.getWaterVolumeOfState(state);
        if (volume < 0) return volume;

        var fluidSection = (FluidSection) ((ExtraStorageSectionContainer) section).getSectionStorage(FluidSection.ID);
        if (fluidSection == null)
            return volume;

        return fluidSection.getWaterVolume(x, y, z);
    }


}
