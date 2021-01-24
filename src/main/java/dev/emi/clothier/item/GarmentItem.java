package dev.emi.clothier.item;

import java.util.List;

import dev.emi.clothier.data.ClothierGarment;
import dev.emi.clothier.data.ClothierMaterial;
import dev.emi.clothier.registry.ClothierItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.registry.Registry;

public class GarmentItem extends Item {

	public GarmentItem(Settings settings) {
		super(settings);
	}

	public static ItemStack of(ClothierGarment garment, List<ClothierMaterial> materials, List<ClothierMaterial> threads) {
		ItemStack stack = new ItemStack(ClothierItems.GARMENT);
		CompoundTag tag = stack.getOrCreateTag();
		tag.putString("Garment", garment.id);
		ListTag mats = new ListTag();
		for (ClothierMaterial mat : materials) {
			mats.add(StringTag.of(Registry.ITEM.getId(mat.item).toString()));
		}
		tag.put("Materials", mats);
		return stack;
	}
}
