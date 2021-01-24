package dev.emi.clothier.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.emi.clothier.ClothierMain;
import dev.emi.clothier.screen.handler.ClothierStationScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ClothierStationScreen extends HandledScreen<ClothierStationScreenHandler> {
	public static final Identifier TEXTURE = new Identifier("clothier", "textures/gui/container/clothier_station.png");
	public int currentScroll = 0;
	public int selectedGarment = -1;

	public ClothierStationScreen(ClothierStationScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		currentScroll += (int) -amount;
		if (currentScroll > ClothierMain.garments.size() - 3) {
			currentScroll = ClothierMain.garments.size() - 3;
		}
		if (currentScroll < 0) {
			currentScroll = 0;
		}
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (mouseX > this.x + 14 && mouseY > this.y + 15 && mouseX <= this.x + 30 && mouseY <= this.y + 69) {
			this.client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
			ClothierStationScreenHandler clothierHandler = (ClothierStationScreenHandler) this.handler;
			int id = ((int) mouseY - this.y - 15) / 18;
			if (id > 2) {
				id = 2;
			}
			id += currentScroll;
			if (id >= 0 && id < ClothierMain.garments.size()) {
				selectedGarment = id;
				this.client.interactionManager.clickButton(clothierHandler.syncId, id);
				clothierHandler.garment = ClothierMain.garments.get(id);
			}
			clothierHandler.updateSlots();
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		this.renderBackground(matrices);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(TEXTURE);
		this.drawTexture(matrices, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
		ClothierStationScreenHandler clothierHandler = (ClothierStationScreenHandler) this.handler;
		for (int i = 37; i < clothierHandler.slots.size(); i++) {
			Slot s = clothierHandler.slots.get(i);
			int vOff = 0;
			if (s.hasStack()) {
				vOff = 36;
			} else if (s instanceof ClothierStationScreenHandler.MaterialSlot) {
				vOff = 18;
			}
			this.drawTexture(matrices, this.x + s.x - 1, this.y + s.y - 1, 16, 166 + vOff, 18, 18);
		}
		int scrollYOff = (39 / (ClothierMain.garments.size() - 3)) * currentScroll;
		int scrollUOff = 0;
		if (ClothierMain.garments.size() < 3) {
			scrollUOff = 12;
		}
		this.drawTexture(matrices, this.x + 33, this.y + 15 + scrollYOff, 176 + scrollUOff, 0, 12, 15);
		for (int i = 0; i < 3; i++) {
			if (i >= ClothierMain.garments.size()) {
				break;
			}
			int vOff = 0;
			if (i + currentScroll == selectedGarment) {
				vOff = 18;
			}
			this.drawTexture(matrices, this.x + 14, this.y + 15 + 18 * i, 0, 166 + vOff, 16, 18);
		}
	}
}
