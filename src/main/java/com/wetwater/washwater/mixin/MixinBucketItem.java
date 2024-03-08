package com.wetwater.washwater.mixin;

import com.wetwater.washwater.FluidManager;
import com.wetwater.washwater.WaterInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(BucketItem.class)
public abstract class MixinBucketItem {

    @Redirect(
            method = "emptyContents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"

            )
    )
    private boolean bucketPlace(Level level, BlockPos pos, BlockState state, int flags) {
        if (!level.isClientSide) {
            FluidManager.addVolume((ServerLevel) level, pos, WaterInfo.volumePerBlock);
            return true;
        } else {
            return false;
        }
    }
}
