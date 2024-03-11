package com.wetwater.washwater;

import com.wetwater.washwater.util.OnInit;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaterMod implements ModInitializer {
	public static final String MODID = "washwater";
    public static final Logger LOGGER = LoggerFactory.getLogger("washwater");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		FluidSection.register();
		OnInit.initializeThings();
	}

	public static ResourceLocation resource(String name) {
		return new ResourceLocation(MODID, name);
	}
}