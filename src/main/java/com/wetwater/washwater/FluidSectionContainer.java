package com.wetwater.washwater;

import org.spongepowered.asm.mixin.Unique;

public interface FluidSectionContainer {

    public FluidSection getFluidSection();

    public void setFluidSection(FluidSection fSection);

}
