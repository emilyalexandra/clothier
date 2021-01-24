package dev.emi.clothier.data;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ClothierMaterial {
	public Item item;
	public int durability;

	public ClothierMaterial(Builder builder) {
		this.item = Registry.ITEM.get(new Identifier(builder.item));
		this.durability = builder.durability;
	}
	
	public class Builder {
		public String item;
		public int durability;
	}
}
