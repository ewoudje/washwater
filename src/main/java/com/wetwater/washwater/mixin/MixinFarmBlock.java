package com.wetwater.washwater.mixin;

import com.wetwater.washwater.FluidManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FarmBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(FarmBlock.class)
public class MixinFarmBlock {

/*    @Inject(at = @At("TAIL"), method = "isNearWater", locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void isNearWater(LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {

        //int prevVol = ((FluidSectionContainer) levelReader.getChunk(blockPos).getSection((blockPos.getY()+ WaterInfo.minY)/16)).getFluidSection().getWaterVolume(blockPos);
        int prevVol = FluidManager.getVolume((Level) levelReader, pos);
        if (prevVol > 99) {
            int newVol = prevVol - 100;
            FluidManager.setVolume((ServerLevel) levelReader, pos, newVol);
        }
        else {
            return false;
        }
    }*/


    @Redirect(method = "randomTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/FarmBlock;isNearWater(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    private boolean checkAndConsumeWater(LevelReader levelReader, BlockPos blockPos) {
        Iterator var2 = BlockPos.betweenClosed(blockPos.offset(-4, 0, -4), blockPos.offset(4, 1, 4)).iterator();
        BlockPos blockPos2;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            blockPos2 = (BlockPos)var2.next();
        } while(FluidManager.getVolume((Level) levelReader, blockPos2) < 50);
        int prevVol = FluidManager.getVolume((Level) levelReader, blockPos2);

        if (prevVol < 50) {
            return false;
        }
        FluidManager.setVolume((ServerLevel) levelReader, blockPos2, prevVol - 50);
        return true;
    }


}
