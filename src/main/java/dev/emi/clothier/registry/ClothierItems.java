package dev.emi.clothier.registry;

import dev.emi.clothier.item.GarmentItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ClothierItems {
	public static final Item GARMENT = register("garment", new GarmentItem(new Item.Settings()));
	
	public static void init() {
	}

	public static Item register(String name, Item item) {
		return Registry.register(Registry.ITEM, new Identifier("clothier", name), item);
	}
}
