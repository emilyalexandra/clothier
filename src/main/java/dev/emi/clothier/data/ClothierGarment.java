package dev.emi.clothier.data;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public class ClothierGarment {
	public String id;
	public EquipmentSlot slot;
	public Identifier trinketSlot;
	public int materials;
	public int threads;

	public ClothierGarment(Builder builder) {
		this.id = builder.id;
		if (builder.slot != null) {
			this.slot = EquipmentSlot.byName(builder.slot);
		} else {
			this.trinketSlot = new Identifier(builder.trinket_slot);
		}
		this.materials = builder.materials;
		this.threads = builder.threads;
	}
	
	public class Builder {
		public String id;
		public String slot;
		public String trinket_slot;
		public int materials;
		public int threads;
	}
}
