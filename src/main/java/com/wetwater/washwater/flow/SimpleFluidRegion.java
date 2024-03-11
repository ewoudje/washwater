package com.wetwater.washwater.flow;

import com.ewoudje.lasagna.chunkstorage.ExtraStorageSectionContainer;
import com.wetwater.washwater.FluidSection;
import com.wetwater.washwater.scheduling.FluidTicker;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;

public class SimpleFluidRegion implements FluidRegion {
    private final Long2ObjectMap<FluidSection> sections = new Long2ObjectOpenHashMap<>();
    private final ServerLevel level;

    public SimpleFluidRegion(ServerLevel level) {
        this.level = level;
    }


    @Override
    public int getVolume(int x, int y, int z) {
        return getSection(x, y, z).getWaterVolume(x & 15, y & 15, z & 15);
    }

    @Override
    public void setVolume(int x, int y, int z, int volume) {
        getSection(x, y, z).setWaterVolume(x & 15, y & 15, z & 15, (short) volume);

        if (volume != 0) {
            FluidTicker.tickWater(level, x, y, z);
            for (Direction direction : Direction.values()) {
                FluidTicker.tickIfWater(level, x + direction.getStepX(), y + direction.getStepY(), z + direction.getStepZ());
            }
        }
    }

    private FluidSection getSection(int x, int y, int z) {
        return sections.computeIfAbsent(
                SectionPos.asLong(x >> 4, y >> 4, z >> 4),
                k -> FluidSection.getOrMake(level.getChunk(x >> 4, z >> 4), level.getSectionIndex(y))
        );
    }
}
