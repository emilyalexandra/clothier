package dev.emi.clothier;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dev.emi.clothier.data.ClothierConfig;
import dev.emi.clothier.data.ClothierGarment;
import dev.emi.clothier.data.ClothierMaterial;
import dev.emi.clothier.registry.ClothierBlocks;
import dev.emi.clothier.registry.ClothierItems;
import dev.emi.clothier.registry.ClothierScreenHandlers;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;

public class ClothierMain implements ModInitializer {
	public static final List<ClothierGarment> garments = Lists.newArrayList();
	public static final Map<String, ClothierGarment> garmentsByName = Maps.newHashMap();
	public static final Map<Item, ClothierMaterial> materials = Maps.newHashMap();
	public static final Map<Item, ClothierMaterial> threads = Maps.newHashMap();

	@Override
	public void onInitialize() {
		ClothierBlocks.init();
		ClothierItems.init();
		ClothierConfig.init();
		ClothierScreenHandlers.init();
	}

	public static void registerGarment(ClothierGarment garment) {
		garments.add(garment);
		garmentsByName.put(garment.id, garment);
	}
	public static void registerMaterial(ClothierMaterial material) {
		materials.put(material.item, material);
	}
	public static void registerThread(ClothierMaterial material) {
		threads.put(material.item, material);
	}
}