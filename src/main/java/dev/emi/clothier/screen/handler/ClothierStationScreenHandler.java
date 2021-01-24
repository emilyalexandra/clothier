package dev.emi.clothier.screen.handler;

import java.util.List;

import com.google.common.collect.Lists;

import dev.emi.clothier.ClothierMain;
import dev.emi.clothier.data.ClothierGarment;
import dev.emi.clothier.data.ClothierMaterial;
import dev.emi.clothier.item.GarmentItem;
import dev.emi.clothier.registry.ClothierScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class ClothierStationScreenHandler extends ScreenHandler {
	public ScreenHandlerContext context;
	public PlayerInventory inventory;
	public ClothierGarment garment = null;
	public CraftingInventory input;
	public SimpleInventory output = new SimpleInventory(1);
	public int threadStart;

	public ClothierStationScreenHandler(int syncId, PlayerInventory inventory) {
		this(syncId, inventory, ScreenHandlerContext.EMPTY);
	}

	public ClothierStationScreenHandler(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
		super(ClothierScreenHandlers.CLOTHIER_STATION, syncId);
		this.context = context;
		this.inventory = inventory;
		int m;
		int l;

		for (m = 0; m < 3; ++m) {
			for (l = 0; l < 9; ++l) {
				this.addSlot(new Slot(inventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
			}
		}

		for (m = 0; m < 9; ++m) {
			this.addSlot(new Slot(inventory, m, 8 + m * 18, 142));
		}

		this.addSlot(new OutputSlot(output, this, 0, 143, 33));
	}

	public void onContentChanged(Inventory inventory) {
		this.context.run((world, blockPos) -> {
			updateResult(world);
		});
	 }

	public void updateResult(World world) {
		if (!world.isClient) {
			ItemStack stack = ItemStack.EMPTY;
			if (this.slots.size() > 37) {
				int total = 0;
				for (int i = 37; i < this.slots.size(); i++) {
					if (this.getSlot(i).getStack().isEmpty()) {
						break;
					}
					total++;
				}
				if (total == input.size()) {
					List<ClothierMaterial> materials = Lists.newArrayList();
					for (int i = 37; i < threadStart; i++) {
						materials.add(ClothierMain.materials.get(getSlot(i).getStack().getItem()));
					}
					stack = GarmentItem.of(garment, materials, List.of());
				}
			}
			this.output.setStack(0, stack);
			((ServerPlayerEntity) inventory.player).networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 36, stack));
		}
	}

	public void onCraft(PlayerEntity player) {
		input.clear();
	}

	public void updateSlots() {
		while (this.slots.size() > 37) {
			Slot s = this.slots.remove(37);
			context.run((world, pos) -> {
				inventory.offerOrDrop(world, s.getStack());
			});
		}
		if (garment != null) {
			input = new CraftingInventory(this, garment.materials + garment.threads, 1);
			this.threadStart = 37 + garment.materials;
			for (int i = 0; i < garment.materials; i++) {
				this.addSlot(new MaterialSlot(input, i, 58 + i * 18, 16));
			}
			for (int i = 0; i < garment.threads; i++) {
				this.addSlot(new ThreadSlot(input, garment.materials + i, 58 + i * 18, 34));
			}
		}
	}

	@Override
	public void close(PlayerEntity player) {
		if (!player.world.isClient) {
			if (input != null) {
				for (int i = 0; i < input.size(); i++) {
					inventory.offerOrDrop(player.world, input.getStack(i));
				}
			}
		}
		super.close(player);
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		if (id >= 0 && id < ClothierMain.garments.size()) {
			this.garment = ClothierMain.garments.get(id);
			output.clear();
		}
		updateSlots();
		return true;
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = (Slot)this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index >= 36) {
				if (!this.insertItem(itemStack2, 0, 36, true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (index >= 0 && index < 27) {
					if (!this.insertItem(itemStack2, 27, 36, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 27 && index < 36 && !this.insertItem(itemStack2, 0, 27, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (itemStack2.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}

			if (itemStack2.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTakeItem(player, itemStack2);
		}

		return itemStack;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}

	public class MaterialSlot extends Slot {

		public MaterialSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			if (ClothierMain.materials.get(stack.getItem()) == null) {
				return false;
			}
			return super.canInsert(stack);
		}

		@Override
		public int getMaxItemCount() {
			return 1;
		}
	}

	public class ThreadSlot extends Slot {

		public ThreadSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			if (ClothierMain.threads.get(stack.getItem()) == null) {
				return false;
			}
			return super.canInsert(stack);
		}

		@Override
		public int getMaxItemCount() {
			return 1;
		}
	}

	public class OutputSlot extends Slot {
		private ClothierStationScreenHandler handler;

		public OutputSlot(Inventory inventory, ClothierStationScreenHandler handler, int index, int x, int y) {
			super(inventory, index, x, y);
			this.handler = handler;
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			return false;
		}

		@Override
		public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
			handler.onCraft(player);
			return super.onTakeItem(player, stack);
		}
	}
}
