package dev.emi.clothier.registry;

import dev.emi.clothier.block.ClothierStation;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ClothierBlocks {
	public static final Block CLOTHIER_STATION = register("clothier_station", new ClothierStation(FabricBlockSettings.copy(Blocks.LOOM)));

	public static void init() {
	}
	
	public static Block register(String name, Block block) {
		Block b = Registry.register(Registry.BLOCK, new Identifier("clothier", name), block);
		Registry.register(Registry.ITEM, new Identifier("clothier", name), new BlockItem(block, new Item.Settings()));
		return b;
	}
}
