package dev.emi.clothier;

import dev.emi.clothier.registry.ClothierScreens;
import dev.emi.clothier.render.ClothierModelProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;

public class ClothierClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClothierScreens.init();
		ClothierResourcePack.init();
		ModelLoadingRegistry.INSTANCE.registerResourceProvider(manager -> new ClothierModelProvider());
	}
}
