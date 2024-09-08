package com.wetwater.washwater.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerLevel.class, priority = 1500) // priority is higher than the target mixin
//@Mixin(value = LevelChunkSection.class, priority = 1500) // priority is higher than the target mixin

public abstract class MixinSquaredChunkSection {


/*    @TargetHandler(
            mixin = "me.jellysquid.mods.lithium.mixin.chunk.block-counting.ChunkSectionMixin",
            name = "anyMatch"
    )
    public <TrackedBlockStatePredicate> boolean anyMatch(TrackedBlockStatePredicate trackedBlockStatePredicate) {
        System.out.println("amager");
        return false;
    }*/
    @TargetHandler(
        mixin = "me.jellysquid.mods.lithium.mixin.chunk.block-counting.ChunkSectionMixin",
        name = "anyMatch",
        prefix = "handler"

    )
    @Inject(method = "@MixinSquared:Handler", at = @At("HEAD"))


}
