package com.sebastian.levoria;

import com.sebastian.levoria.block.ModBlocks;
import com.sebastian.levoria.block.entity.ModBlockEntities;
import com.sebastian.levoria.item.ModDataComponentTypes;
import com.sebastian.levoria.item.ModItemGroups;
import com.sebastian.levoria.item.ModItems;
import com.sebastian.levoria.network.HighlightBlockS2C;
import com.sebastian.levoria.network.TotemAnimationS2C;
import com.sebastian.levoria.world.custom.ShadowTreeTrunkPlacer;
import com.sebastian.levoria.world.gen.ModWorldGen;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Levoria implements ModInitializer {
	public static final String MOD_ID = "levoria";
	public static final String NAME = "Levoria";
	public static final String VERSION = "1.1.0";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	//tree trunk placers
	public static final TrunkPlacerType<ShadowTreeTrunkPlacer> SHADOW_TRUNK_PLACER = new TrunkPlacerType<>(ShadowTreeTrunkPlacer.CODEC);

	@Override
	public void onInitialize() {

		//Important -> Register Custom Tree Trunk Placers First!
		Registry.register(Registries.TRUNK_PLACER_TYPE, Levoria.getId("shadow_tree_trunk_placer"), SHADOW_TRUNK_PLACER);

		//Main Registration

		ModItems.registerModItems();
		ModDataComponentTypes.registerDataComponentTypes();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerModBlockEntities();
		ModItemGroups.registerModItemGrops();
		ModWorldGen.generateModWorldGen();


		//PACKETS
		PayloadTypeRegistry.playS2C().register(TotemAnimationS2C.ID, TotemAnimationS2C.PACKET_CODEC);
		PayloadTypeRegistry.playS2C().register(HighlightBlockS2C.ID, HighlightBlockS2C.PACKET_CODEC);

		//SELF-EXPLANATORY

		CompostingChanceRegistry.INSTANCE.add(ModItems.MOON_BERRIES, 0.65f);

		StrippableBlockRegistry.register(ModBlocks.SHADOW_WOOD_LOG, ModBlocks.STRIPPED_SHADOW_WOOD_LOG);
		StrippableBlockRegistry.register(ModBlocks.SHADOW_WOOD_WOOD, ModBlocks.STRIPPED_SHADOW_WOOD_WOOD);

		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.SHADOW_WOOD_LOG, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.SHADOW_WOOD_WOOD, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.STRIPPED_SHADOW_WOOD_LOG, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.STRIPPED_SHADOW_WOOD_WOOD, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.SHADOW_WOOD_PLANKS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.SHADOW_WOOD_LEAVES, 30, 60);
	}

	public static Identifier getId(String sub) {
		return Identifier.of(MOD_ID, sub);
	}
}