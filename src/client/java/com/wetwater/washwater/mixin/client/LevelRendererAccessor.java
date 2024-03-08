package com.wetwater.washwater.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(net.minecraft.client.renderer.LevelRenderer.class)
public interface LevelRendererAccessor {
    @Invoker("setSectionDirty")
    void iLoveItDirty(int x, int y, int z, boolean important);
}
