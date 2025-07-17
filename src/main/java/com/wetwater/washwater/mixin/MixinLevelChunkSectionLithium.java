package com.wetwater.washwater.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.wetwater.washwater.FluidSection;
import com.wetwater.washwater.FluidSectionContainer;
import com.wetwater.washwater.WaterInfo;
import com.wetwater.washwater.state.WashWaterFluidState;
import me.jellysquid.mods.lithium.common.block.TrackedBlockStatePredicate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LevelChunkSection.class, priority = 1200)
public class MixinLevelChunkSectionLithium implements FluidSectionContainer {
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

    //LITHIUM COMPAT

/*    @Inject(at = @At("RETURN"), method = "maybeHas", cancellable = true)
    public void maybeHas(Predicate<BlockState> predicate, CallbackInfoReturnable<Boolean> cir) {
        //System.out.println("Test1");
        if (fluidSection != null && !cir.getReturnValue()) {
            //System.out.println("Test2");
            if(predicate.test(Blocks.WATER.defaultBlockState())) {
                //System.out.println("Test3");
                cir.setReturnValue(!fluidSection.isEmpty());
            }
        }
    }*/


    public boolean anyMatch(TrackedBlockStatePredicate predicate, boolean cir) {
        if (fluidSection != null && !cir) {
            if(predicate.test(Blocks.WATER.defaultBlockState())) {
                cir = (!fluidSection.isEmpty());
            }
        }
        return cir;
    }
/*    @Override
    public boolean anyMatch(TrackedBlockStatePredicate predicate, boolean cir) {
        if (fluidSection != null && !cir) {
            if(predicate.test(Blocks.WATER.defaultBlockState())) {
                cir = (!fluidSection.isEmpty());
            }
        }
        return cir;
    }*/

    // END

    /**
     * @author
     * @reason
     */
/*    @Overwrite
    public boolean maybeHas(Predicate<BlockState> predicate) {
        System.out.println("Test1");
        if (fluidSection != null) {
            System.out.println("Test2");
            if (predicate.test(Blocks.WATER.defaultBlockState())) {
                System.out.println("Test3");
                return !fluidSection.isEmpty();
            }
        }
        return false;
    }*/


    @ModifyReturnValue(at = @At("RETURN"), method = "hasOnlyAir")
    public boolean hasOnlyAir(boolean original) {
        return original && (fluidSection == null || fluidSection.isEmpty());
    }
}
