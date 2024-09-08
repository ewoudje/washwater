package com.wetwater.washwater.mixin;

import com.wetwater.washwater.FluidManager;
import com.wetwater.washwater.WaterInfo;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.Path;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GroundPathNavigation.class)
public abstract class NavigationMixin extends PathNavigation {


    public NavigationMixin(Mob mob, Level level) {
        super(mob, level);
    }

    @Redirect(
            method = "createPath(Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/world/level/pathfinder/Path;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;")
    )
    public BlockState getBlockState(Level instance, BlockPos blockPos) {
        System.out.println("sanity check");
        FluidState fs = instance.getBlockState(blockPos).getFluidState();
        System.out.println("height = " + fs.getOwnHeight());
        if (fs == Fluids.WATER.defaultFluidState())
            System.out.println("pathfinding internal block is water");
        return instance.getBlockState(blockPos);
    }


/*    @Inject(at = @At("HEAD"), method = "createPath(Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/world/level/pathfinder/Path;")
    public void createPath(BlockPos blockPos, int i, CallbackInfoReturnable<Path> cir) {
        GroundPathNavigation thisObject = (GroundPathNavigation) (Object)this;
        System.out.println("mixin print: " + thisObject.);

    }*/


}
