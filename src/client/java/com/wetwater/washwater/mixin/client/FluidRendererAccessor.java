package com.wetwater.washwater.mixin.client;

import me.jellysquid.mods.sodium.client.model.light.LightPipelineProvider;
import me.jellysquid.mods.sodium.client.model.quad.blender.ColorBlender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(me.jellysquid.mods.sodium.client.render.pipeline.FluidRenderer.class)
public interface FluidRendererAccessor {

    @Accessor("lighters")
    LightPipelineProvider getLighters();
    @Accessor("colorBlender")
    ColorBlender getColorBlender();
}
