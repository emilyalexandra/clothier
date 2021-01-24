package dev.emi.clothier.render;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;

import dev.emi.clothier.ClothierMain;
import dev.emi.clothier.data.ClothierGarment;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

public class GarmentModel implements UnbakedModel, BakedModel, FabricBakedModel {
	private static final Identifier ITEM_HANDHELD = new Identifier("minecraft:item/handheld");
	private ModelTransformation transformation;

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
	}
	
	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
		CompoundTag tag = stack.getTag();
		if (!stack.hasTag() || !tag.contains("Garment") || !tag.contains("Materials")) {
			context.fallbackConsumer().accept(MinecraftClient.getInstance().getBakedModelManager().getMissingModel());
			return;
		}
		String g = tag.getString("Garment");
		ClothierGarment garment = ClothierMain.garmentsByName.get(g);
		ListTag materials = tag.getList("Materials", 8);
		if (garment == null || materials.size() < garment.materials) {
			context.fallbackConsumer().accept(MinecraftClient.getInstance().getBakedModelManager().getMissingModel());
			return;
		}
		for (int i = 0; i < garment.materials; i++) {
			String mat = materials.getString(i).replace(":", "/");
			ModelIdentifier id = new ModelIdentifier("clothier:" + g + "/" + mat + "/" + i + "#inventory");
			BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(id);
	
			context.fallbackConsumer().accept(model);
		}
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction face, Random random) {
		return null;
	}

	@Override
	public boolean useAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean hasDepth() {
		return false;
	}

	@Override
	public boolean isSideLit() {
		return false;
	}

	@Override
	public boolean isBuiltin() {
		return false;
	}

	@Override
	public Sprite getSprite() {
		return null;
	}

	@Override
	public ModelTransformation getTransformation() {
		return transformation;
	}

	@Override
	public ModelOverrideList getOverrides() {
		return ModelOverrideList.EMPTY;
	}

	@Override
	public Collection<Identifier> getModelDependencies() {
		return Arrays.asList(ITEM_HANDHELD, new ModelIdentifier("clothier:helmet/minecraft/leather/1#inventory"));
	}
	
	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
		return Collections.emptyList();
	}

	@Override
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier id) {
		transformation = ((JsonUnbakedModel) loader.getOrLoadModel(ITEM_HANDHELD)).getTransformations();
		return this;
	}
}
