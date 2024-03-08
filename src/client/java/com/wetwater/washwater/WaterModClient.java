package com.wetwater.washwater;

import net.fabricmc.api.ClientModInitializer;

public class WaterModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientNetworking.register();
	}
}