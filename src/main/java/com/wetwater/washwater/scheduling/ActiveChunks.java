package com.wetwater.washwater.scheduling;

import com.google.common.collect.Lists;
import com.wetwater.washwater.mixin.ChunkMapAccessor;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ActiveChunks {
    public static void getActiveChunks(ServerLevel level, LongSet current) {
        ServerChunkCache chunkSource = level.getChunkSource();
        var loadedChunksList = ((ChunkMapAccessor) chunkSource.chunkMap).getAllChunks();
        for (var chunkHolder : loadedChunksList) {
            final Optional<LevelChunk> worldChunkOptional =
                    chunkHolder.getTickingChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK).left();

            if (worldChunkOptional.isPresent()) {
                final LevelChunk worldChunk = worldChunkOptional.get();
                current.add(worldChunk.getPos().toLong());
            }
        }
    }
}
