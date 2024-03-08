package com.wetwater.washwater.scheduling;

import com.wetwater.washwater.FluidManager;
import com.wetwater.washwater.flow.FluidFlow;
import com.wetwater.washwater.flow.FluidRegion;
import com.wetwater.washwater.flow.SimpleFluidRegion;
import com.wetwater.washwater.util.SwapPair;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.io.FileSystem.getCurrent;

public class FluidTicker {
    private static int counter = 0;
    private static final Map<ServerLevel, FluidRegion> regions = new HashMap<>();
    private static final Map<ServerLevel, SwapPair<LongSet>> waters = new HashMap<>();

    public static void tickWater(ServerLevel level, BlockPos pos) {
        getCurrentWaterList(level).add(pos.asLong());
    }

    public static void tickWater(ServerLevel level, int x, int y, int z) {
        getCurrentWaterList(level).add(BlockPos.asLong(x, y, z));
    }

    public static boolean shouldTick(ServerLevel level) {
        return counter % FluidManager.tickSpeed(level) == 0;
    }

    public static boolean shouldClearRegions(ServerLevel level) {
        return counter % (FluidManager.tickSpeed(level) * 100) == 0;
    }

    public static void tick(ServerLevel level) {
        if(shouldTick(level)) {
            LongSet activeChunks = new LongOpenHashSet();
            ActiveChunks.getActiveChunks(level, activeChunks);

            var region = regions.computeIfAbsent(level, SimpleFluidRegion::new);
            var pair = waters.get(level);
            if (pair == null) return;

            pair.swap();
            for (long pos : pair.getOther()) {
                var bPos = BlockPos.of(pos);
                var chunkPos = ChunkPos.asLong(bPos.getX() >> 4, bPos.getZ() >> 4);

                //if (activeChunks.contains(chunkPos)) {
                    FluidFlow.tick(region, bPos);
                //}
            }

            pair.getOther().clear();

        }

        if (shouldClearRegions(level)) {
            regions.clear();
            counter = 0;
        }

        counter++;
    }

    private static LongSet getCurrentWaterList(ServerLevel level) {
        return waters.computeIfAbsent(level, k -> new SwapPair<>(new LongOpenHashSet(), new LongOpenHashSet())).getCurrent();
    }
}
