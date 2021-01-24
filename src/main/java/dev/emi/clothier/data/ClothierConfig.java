package dev.emi.clothier.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dev.emi.clothier.ClothierMain;

public class ClothierConfig {

	public static void init() {
		try {
			File dir = new File("config");
			dir.mkdirs();
			File config = new File("config/clothier.json");
			if (!config.exists()) {
				config.createNewFile();
				OutputStream os = new FileOutputStream(config);
				InputStream is = ClothierConfig.class.getClassLoader().getResourceAsStream("config.json");
				byte[] buffer = new byte[1024];
				while (true) {
					int bytesRead = is.read(buffer);
					if (bytesRead == -1) {
						break;
					}
					os.write(buffer, 0, bytesRead);
				}
				is.close();
				os.close();
			}
			Gson gson = new Gson();
			JsonObject root = gson.fromJson(new FileReader("config/clothier.json"), JsonObject.class);
			JsonArray list = root.getAsJsonArray("garments");
			for (JsonElement j: list) {
				ClothierGarment garment = new ClothierGarment(gson.fromJson(j, ClothierGarment.Builder.class));
				ClothierMain.registerGarment(garment);
			}
			// TODO materials and threads should be datapacks and not config
			list = root.getAsJsonArray("materials");
			for (JsonElement j: list) {
				ClothierMaterial material = new ClothierMaterial(gson.fromJson(j, ClothierMaterial.Builder.class));
				ClothierMain.registerMaterial(material);
			}
			list = root.getAsJsonArray("threads");
			for (JsonElement j: list) {
				ClothierMaterial material = new ClothierMaterial(gson.fromJson(j, ClothierMaterial.Builder.class));
				ClothierMain.registerThread(material);
			}
		} catch (Exception e) {
			throw new RuntimeException("Clothier config failed to parse", e);
		}
	}
}
