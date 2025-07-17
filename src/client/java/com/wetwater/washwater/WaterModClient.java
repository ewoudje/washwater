package com.wetwater.washwater;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

import static com.wetwater.washwater.item.ModItems.PRECISION_BUCKET;

public class WaterModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientNetworking.register();
		registerItemProperties();

	}

	public static void registerItemProperties() {
		ItemProperties.register(PRECISION_BUCKET, new ResourceLocation("bucketlevel"), (itemStack, clientWorld, livingEntity, seed) -> {
			return itemStack.getOrCreateTag().getInt("washwater:bucketFillLevel")/1000f;
		});

/*		FabricModelPredicateProviderRegistry.register(PRECISION_BUCKET, new ResourceLocation("bucketlevel"), (itemStack, clientWorld, livingEntity, seed) -> {
			System.out.println("pretest: " + (livingEntity.getUseItem() == itemStack ? livingEntity.getUseItem().getTag().getInt("washwater:bucketFillLevel") : 0.1f));
			//System.out.println("test: " + livingEntity.getUseItem().getOrCreateTag().getInt("washwater:bucketFillLevel")/1000f);
			return livingEntity.getUseItem().getOrCreateTag().getInt("washwater:bucketFillLevel")/1000f;
			//return livingEntity.getUseItem() != itemStack ? 0.0F : (itemStack.isDamaged()) / 20.0F;
			//return 1.0f;
		});*/
/*		ModelPredicateProviderRegistry.register(EXAMPLE_BOW, Identifier.ofVanilla("pulling"), (itemStack, clientWorld, livingEntity, seed) -> {
			if (livingEntity == null) {
				return 0.0F;
			}
			return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
		});*/
	}


}