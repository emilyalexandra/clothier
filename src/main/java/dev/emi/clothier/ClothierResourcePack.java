package dev.emi.clothier;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import dev.emi.clothier.data.ClothierGarment;
import dev.emi.clothier.data.ClothierMaterial;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ClothierResourcePack {

	public static void init() {
		RRPCallback.EVENT.register(p -> addPack(p));
		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((texture, registry) -> {
			for (ClothierGarment garment : ClothierMain.garments) {
				for (Map.Entry<Item, ClothierMaterial> entry : ClothierMain.materials.entrySet()) {
					Item item = entry.getKey();
					String path = Registry.ITEM.getId(item).toString().replace(":", "/");
					for (int i = 0; i < garment.materials; i++) {
						Identifier id = new Identifier("clothier", "clothier_parts/" + garment.id + "/" + path + "/" + i);
						registry.register(id);
					}
				}
			}
		});
		ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, consumer) -> {
			for (ClothierGarment garment : ClothierMain.garments) {
				for (Map.Entry<Item, ClothierMaterial> entry : ClothierMain.materials.entrySet()) {
					Item item = entry.getKey();
					String path = Registry.ITEM.getId(item).toString().replace(":", "/");
					for (int i = 0; i < garment.materials; i++) {
						ModelIdentifier id = new ModelIdentifier("clothier" + ":" + garment.id + "/" + path + "/" + i + "#inventory");
						consumer.accept(id);
					}
				}
			}
		});
	}

	public static void addPack(List<ResourcePack> list) {
		RuntimeResourcePack pack = RuntimeResourcePack.create("clothier:clothier");
		for (ClothierGarment garment : ClothierMain.garments) {
			for (Map.Entry<Item, ClothierMaterial> entry : ClothierMain.materials.entrySet()) {
				Item item = entry.getKey();
				String path = Registry.ITEM.getId(item).toString().replace(":", "/");
				for (int i = 0; i < garment.materials; i++) {
					Identifier id = new Identifier("clothier", "textures/clothier_parts/" + garment.id + "/" + path + "/" + i + ".png");
					final int index = i;
					pack.addLazyResource(ResourceType.CLIENT_RESOURCES, id, (p, identifier) -> {
						try {
							ResourceManager manager = MinecraftClient.getInstance().getResourceManager();
							Resource g = manager.getResource(new Identifier("clothier", "textures/clothier/garment/" + garment.id + "/materials/" + index + ".png"));
							Resource l = manager.getResource(new Identifier("clothier", "textures/clothier/material/" + path + "/lights.png"));
							Resource m = manager.getResource(new Identifier("clothier", "textures/clothier/material/" + path + "/mids.png"));
							Resource d = manager.getResource(new Identifier("clothier", "textures/clothier/material/" + path + "/darks.png"));
							BufferedImage gi = ImageIO.read(g.getInputStream());
							BufferedImage li = ImageIO.read(l.getInputStream());
							BufferedImage mi = ImageIO.read(m.getInputStream());
							BufferedImage di = ImageIO.read(d.getInputStream());
							for (int x = 0; x < gi.getWidth(); x++) {
								for (int y = 0; y < gi.getHeight(); y++) {
									int color = gi.getRGB(x, y);
									int lightAmount = (color & 0xff0000) >> 16;
									int midAmount = (color & 0xff00) >> 8;
									int darkAmount = color & 0xff;
									int alpha = (color & 0xff000000) >>> 24;
									int totalAmount = lightAmount + midAmount + darkAmount;
									if (garment.id.equals("thigh_highs") && item == Items.PURPLE_WOOL && x == 5 && y == 6) {
										System.out.println(index);
										System.out.println("a:");
										System.out.println(lightAmount);
										System.out.println(midAmount);
										System.out.println(darkAmount);
										System.out.println(alpha);
									}
					
									if (totalAmount == 0) {
										continue;
									}
					
									int outRed = 0, outGreen = 0, outBlue = 0;
					
									if (lightAmount > 0) {
										int lColor = li.getRGB(x, y);
										int lRed = (lColor & 0xff0000) >> 16;
										int lGreen = (lColor & 0xff00) >> 8;
										int lBlue = lColor & 0xff;
										int lAlpha = (lColor & 0xff000000) >>> 24;
										outRed += lRed * lightAmount / totalAmount * lAlpha / 255;
										outGreen += lGreen * lightAmount / totalAmount * lAlpha / 255;
										outBlue += lBlue * lightAmount / totalAmount * lAlpha / 255;
										if (garment.id.equals("thigh_highs") && item == Items.PURPLE_WOOL && x == 5 && y == 6) {
											System.out.println("l:");
											System.out.println(lRed);
											System.out.println(lGreen);
											System.out.println(lBlue);
										}
									}
					
									if (midAmount > 0) {
										int mColor = mi.getRGB(x, y);
										int mRed = (mColor & 0xff0000) >> 16;
										int mGreen = (mColor & 0xff00) >> 8;
										int mBlue = mColor & 0xff;
										int mAlpha = (mColor & 0xff000000) >>> 24;
										outRed += mRed * midAmount / totalAmount * mAlpha / 255;
										outGreen += mGreen * midAmount / totalAmount * mAlpha / 255;
										outBlue += mBlue * midAmount / totalAmount * mAlpha / 255;
										if (garment.id.equals("thigh_highs") && item == Items.PURPLE_WOOL && x == 5 && y == 6) {
											System.out.println("m:");
											System.out.println(mRed);
											System.out.println(mGreen);
											System.out.println(mBlue);
										}
									}
					
									if (darkAmount > 0) {
										int dColor = di.getRGB(x, y);
										int dRed = (dColor & 0xff0000) >> 16;
										int dGreen = (dColor & 0xff00) >> 8;
										int dBlue = dColor & 0xff;
										int dAlpha = (dColor & 0xff000000) >>> 24;
										outRed += dRed * darkAmount / totalAmount * dAlpha / 255;
										outGreen += dGreen * darkAmount / totalAmount * dAlpha / 255;
										outBlue += dBlue * darkAmount / totalAmount * dAlpha / 255;
										if (garment.id.equals("thigh_highs") && item == Items.PURPLE_WOOL && x == 5 && y == 6) {
											System.out.println("d:");
											System.out.println(dRed);
											System.out.println(dGreen);
											System.out.println(dBlue);
										}
									}

									if (garment.id.equals("thigh_highs") && item == Items.PURPLE_WOOL && x == 5 && y == 6) {
										System.out.println("f:");
										System.out.println(outRed);
										System.out.println(outGreen);
										System.out.println(outBlue);
									}
					
									color = (outRed << 16) | (outGreen << 8) | outBlue | (alpha << 24);
									gi.setRGB(x, y, color);
								}
							}
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ImageIO.write(gi, "png", baos);
							byte[] in = baos.toByteArray();
							return in;
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					});
					Identifier modelId = new Identifier("clothier", "item/" + garment.id + "/" + path + "/" + i);
					pack.addModel(JModel.model("minecraft:item/handheld").textures(new JTextures().layer0(new Identifier("clothier", "clothier_parts/" + garment.id + "/" + path + "/" + i).toString())), modelId);
				}
			}
		}
		list.add(pack);
	}
}
