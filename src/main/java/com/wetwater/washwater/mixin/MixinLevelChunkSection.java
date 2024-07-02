package com.wetwater.washwater.mixin;

import com.ewoudje.lasagna.chunkstorage.ExtraStorageSectionContainer;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.wetwater.washwater.FluidSection;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunkSection.class)
public class MixinLevelChunkSection {
    @Unique
    private FluidSection fluidSection;

    @Inject(at = @At("HEAD"), method = "setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;")
    public void setBlockState(int x, int y, int z, BlockState state, boolean lock, CallbackInfoReturnable<BlockState> cir) {
        if (fluidSection == null) //TODO we should make this smarter, just set the section when configuring the watersection
            fluidSection = (FluidSection) ((ExtraStorageSectionContainer) this).getSectionStorage(FluidSection.ID);

        if (fluidSection != null)
            fluidSection.setWaterVolumeByState(x, y, z, state);
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "hasOnlyAir")
    public boolean hasOnlyAir(boolean original) {
        return original && (fluidSection == null || fluidSection.isEmpty());
    }
}
