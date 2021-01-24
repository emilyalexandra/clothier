package dev.emi.clothier.registry;

import java.util.function.BiFunction;

import dev.emi.clothier.screen.handler.ClothierStationScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ClothierScreenHandlers {
	public static final ScreenHandlerType<ClothierStationScreenHandler> CLOTHIER_STATION = register("clothier_station", ClothierStationScreenHandler::new);
	
	public static void init() {
	}

	public static <T extends ScreenHandler> ScreenHandlerType<T> register(String name, BiFunction<Integer, PlayerInventory, T> supplier) {
		return ScreenHandlerRegistry.registerSimple(new Identifier("clothier", name), new ScreenHandlerRegistry.SimpleClientHandlerFactory<T>() {
			public T create(int syncId, PlayerInventory inventory) {
				return supplier.apply(syncId, inventory);
			}
		});
	}
}
