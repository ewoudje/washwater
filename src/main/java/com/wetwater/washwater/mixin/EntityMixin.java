package com.wetwater.washwater.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract Vec3 position();

    @Shadow private BlockPos blockPosition;

    @Shadow public Level level;

    @Shadow protected boolean wasTouchingWater;

    @Shadow public abstract Level getLevel();

    @Shadow public abstract Vec3 getPosition(float f);

    @Shadow @Final protected SynchedEntityData entityData;

    @Inject(at = @At("HEAD"), method = "isColliding")
    public void isColliding(BlockPos blockPos, BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        //System.out.println("mixine called");
    }
    @Inject(at = @At("HEAD"), method = "baseTick")
    public void baseTick(CallbackInfo ci) {
        BlockPos pos = this.blockPosition;
        if (this.level.getBlockState(pos) == Blocks.WATER.defaultBlockState() && !this.level.isClientSide) {
            //System.out.println("entity is in water");
        }
    }
    @Inject(at = @At("HEAD"), method = "isInWater")
    public void isInWater (CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof AbstractSchoolingFish) {
            //System.out.println("was touching water: " + this.wasTouchingWater);
            FluidState fs = entityIn(this.getLevel(), this.getPosition(1f));
            //System.out.println("pos is: " + this.getPosition(1f) + "or " + position());
            //System.out.println("is in water: " + fs);
        }
    }

    public FluidState entityIn(Level level, Vec3 pos) {
       return level.getFluidState(new BlockPos(pos.x, pos.y, pos.z));
    }

    /**
     * @author
     * @reason
     */
/*
    @Overwrite
    public void updateInWaterStateAndDoWaterCurrentPushing() {
        //System.out.println("set touch to true");
        this.wasTouchingWater = true;
    }
*/

    //@Inject(at = @At("HEAD"), method = "updateFluidHeightAndDoFluidPushing")



}
