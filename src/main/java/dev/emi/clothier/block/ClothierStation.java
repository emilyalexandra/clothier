package dev.emi.clothier.block;

import dev.emi.clothier.screen.handler.ClothierStationScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ClothierStation extends BlockWithEntity {

	public ClothierStation(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> {
				return new ClothierStationScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos));
			}, new TranslatableText("clothier.container.clothier_station")));
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return null;
	}
}
