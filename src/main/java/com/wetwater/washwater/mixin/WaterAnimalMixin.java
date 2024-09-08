package com.wetwater.washwater.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class WaterAnimalMixin {

    @Shadow public abstract Vec3 position();


    @Shadow private BlockPos blockPosition;

    @Shadow public Level level;

    @Shadow protected boolean wasTouchingWater;

    @Shadow public abstract Level getLevel();

    @Shadow public abstract int getAirSupply();

    @Shadow public abstract Vec3 getPosition(float f);

    @Shadow @Final protected SynchedEntityData entityData;

    @Shadow public abstract boolean isInWater();

    @Inject(at = @At("HEAD"), method = "baseTick")
    public void baseTick(CallbackInfo ci) {
        if ((Object) this instanceof AbstractSchoolingFish && !this.level.isClientSide) {
            //System.out.println("entity air supply: " + this.getAirSupply());
            //System.out.println("is in water : " + this.isInWater());
            //System.out.println("was touching: " + wasTouchingWater );
            FluidState fs = entityIn(this.getLevel(), this.getPosition(1f));
            //System.out.println("custom check: " + (fs == Fluids.WATER.defaultFluidState()));
        }

    }

    public FluidState entityIn(Level level, Vec3 pos) {
        return level.getFluidState(new BlockPos(pos.x, pos.y, pos.z));
    }



}
