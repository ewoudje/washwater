package com.wetwater.washwater.debug;

import com.ewoudje.lasagna.chunkstorage.ExtraStorageSectionContainer;
import com.wetwater.washwater.FluidSection;
import net.minecraft.server.level.ServerLevel;

public class DebugUtils {

    public static boolean debugMethodCheckNullSection(ServerLevel level, int x, int y, int z) {
        var chunk = level.getChunk(x >> 4, z >> 4);
        var section = chunk.getSections()[level.getSectionIndex(y)];
        FluidSection fSection = (FluidSection) ((ExtraStorageSectionContainer) section).getSectionStorage(FluidSection.ID);
        return fSection == null;
    }
    public static boolean debugMethodCheckEmptySection(ServerLevel level, int x, int y, int z) {
        var chunk = level.getChunk(x >> 4, z >> 4);
        var section = chunk.getSections()[level.getSectionIndex(y)];
        FluidSection fSection = (FluidSection) ((ExtraStorageSectionContainer) section).getSectionStorage(FluidSection.ID);
        return fSection.isEmpty();
    }

}
