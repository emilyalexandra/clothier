package dev.emi.clothier.render;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;

public class ClothierModelProvider implements ModelResourceProvider {
	public static final GarmentModel GARMENT_MODEL = new GarmentModel();
	public static final Identifier GARMENT_ITEM_MODEL = new Identifier("clothier:item/garment");

	@Override
	public @Nullable UnbakedModel loadModelResource(Identifier id, ModelProviderContext context) throws ModelProviderException {
		if (id.equals(GARMENT_ITEM_MODEL)) {
			return GARMENT_MODEL;
		}
		return null;
	}	
}
