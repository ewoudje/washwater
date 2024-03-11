package com.wetwater.washwater;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaterMod implements ModInitializer {
	public static final String MODID = "washwater";
    public static final Logger LOGGER = LoggerFactory.getLogger("washwater");
	public static long currentTick = 0;

	@Override
	public void onInitialize() {
		FluidSection.register();
	}

	public static ResourceLocation resource(String name) {
		return new ResourceLocation(MODID, name);
	}
}