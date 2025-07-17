package com.wetwater.washwater.mixin;

import com.wetwater.washwater.WaterInfo;
import com.wetwater.washwater.state.WashWaterFluidState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.wetwater.washwater.WaterInfo.volumePerBlock;


@Mixin(FluidState.class)
class FluidStateMixin implements WashWaterFluidState {
    @Unique
    private short ww$volume = 0;

    @Override
    @Unique
    public short ww$getVolume() {
        return ww$volume;
    }

    @Override
    @Unique
    public void ww€setVolume(short volume) {
        ww$volume = volume;
    }

    /**
     * @SirWashington
     * @We redefine this
     */
    @Overwrite
    public float getHeight(BlockGetter blockGetter, BlockPos blockPos) {
        if (ww$volume <= 0) return 0;
        return ((float) ww$volume) / volumePerBlock;
    }
}
