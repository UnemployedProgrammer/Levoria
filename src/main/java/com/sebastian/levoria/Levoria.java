package com.sebastian.levoria;

import com.sebastian.levoria.block.ModBlocks;
import com.sebastian.levoria.block.entity.ModBlockEntities;
import com.sebastian.levoria.item.ModDataComponentTypes;
import com.sebastian.levoria.item.ModItems;
import com.sebastian.levoria.network.HighlightBlockS2C;
import com.sebastian.levoria.network.TotemAnimationS2C;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Levoria implements ModInitializer {
	public static final String MOD_ID = "levoria";
	public static final String NAME = "Levoria";
	public static final String VERSION = "1.1.0";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModDataComponentTypes.registerDataComponentTypes();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerModBlockEntities();


		//PACKETS
		PayloadTypeRegistry.playS2C().register(TotemAnimationS2C.ID, TotemAnimationS2C.PACKET_CODEC);
		PayloadTypeRegistry.playS2C().register(HighlightBlockS2C.ID, HighlightBlockS2C.PACKET_CODEC);

	}

	public static Identifier getId(String sub) {
		return Identifier.of(MOD_ID, sub);
	}
}