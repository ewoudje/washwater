package com.wetwater.washwater.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.wetwater.washwater.FluidSection;
import com.wetwater.washwater.FluidSectionContainer;
import com.wetwater.washwater.WaterInfo;
import com.wetwater.washwater.state.WashWaterFluidState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LevelChunkSection.class, priority = 1200)
public class MixinLevelChunkSection implements FluidSectionContainer {
    @Unique
    private FluidSection fluidSection;

    @Unique
    public FluidSection getFluidSection() {
        return this.fluidSection;
    }

    @Unique
    public void setFluidSection(FluidSection fSection) {
        this.fluidSection = fSection;
    }

    @Inject(at = @At("HEAD"), method = "setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;")
    public void setBlockState(int x, int y, int z, BlockState state, boolean lock, CallbackInfoReturnable<BlockState> cir) {
        if (fluidSection != null)
            fluidSection.setWaterVolumeByState(x, y, z, state);
    }

    @Inject(at = @At("RETURN"), method = "getBlockState", cancellable = true)
    public void getBlockState(int x, int y, int z, CallbackInfoReturnable<BlockState> cir) {
        if (fluidSection != null && cir.getReturnValue().isAir()) {
            WashWaterFluidState waterState = ((WashWaterFluidState)(Object)WaterInfo.getWaterState(fluidSection.getWaterVolume(x, y, z)));
            waterState.ww€setVolume(fluidSection.getWaterVolume(x, y, z));
            cir.setReturnValue((((FluidState)(Object)waterState).createLegacyBlock()));
        }

    }

    @Inject(at = @At("RETURN"), method = "getFluidState", cancellable = true)
    public void getFluidState(int x, int y, int z, CallbackInfoReturnable<FluidState> cir) {
        if (fluidSection != null && cir.getReturnValue().isEmpty()) {
            WashWaterFluidState waterState = ((WashWaterFluidState)(Object)WaterInfo.getWaterState(fluidSection.getWaterVolume(x, y, z)));
            waterState.ww€setVolume(fluidSection.getWaterVolume(x, y, z));
            cir.setReturnValue(((FluidState)(Object)waterState));
        }
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "hasOnlyAir")
    public boolean hasOnlyAir(boolean original) {
        return original && (fluidSection == null || fluidSection.isEmpty());
    }
}
