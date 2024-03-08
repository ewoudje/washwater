package com.wetwater.washwater.mixin.client;

import com.wetwater.washwater.FluidManager;
import com.wetwater.washwater.WashFluidRenderer;
import com.wetwater.washwater.WaterInfo;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.tasks.ChunkRenderRebuildTask;
import me.jellysquid.mods.sodium.client.render.pipeline.FluidRenderer;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkRenderRebuildTask.class)
public class ChunkRenderRebuildTaskMixin {


    @Unique
    private int fluidVolume = 0;

    @Redirect(
            method = "performBuild",
            at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/world/WorldSlice;getBlockState(III)Lnet/minecraft/world/level/block/state/BlockState;")
    )
    BlockState getBlockState(WorldSlice slice, int x, int y, int z) {
        BlockState state = slice.getBlockState(x,y,z);
        fluidVolume = FluidManager.getVolume(((WorldSliceAccessor) slice).getLevel(), x, y, z);
        return state.isAir() && fluidVolume != -1 ? WaterInfo.getWaterState(fluidVolume).createLegacyBlock() : state;
    }

    @Redirect(
            method = "performBuild",
            at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/render/pipeline/FluidRenderer;render(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lme/jellysquid/mods/sodium/client/render/chunk/compile/buffers/ChunkModelBuilder;)Z")
    )
    boolean getFluidState(FluidRenderer instance, BlockAndTintGetter level, FluidState state, BlockPos pos, BlockPos rel, ChunkModelBuilder builder) {
        return WashFluidRenderer.getInstance(instance).render(((WorldSliceAccessor) level).getLevel(), pos, rel, builder, fluidVolume);
    }

}
