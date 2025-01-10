package com.sebastian.levoria;

import com.sebastian.levoria.block.ModBlocks;
import com.sebastian.levoria.block.entity.DoorMatBlockEntity;
import com.sebastian.levoria.block.entity.ModBlockEntities;
import com.sebastian.levoria.config.ConfigEditor;
import com.sebastian.levoria.config.ConfigManager;
import com.sebastian.levoria.enchantment.ModEnchantmentEffects;
import com.sebastian.levoria.item.ModDataComponentTypes;
import com.sebastian.levoria.item.ModItemGroups;
import com.sebastian.levoria.item.ModItems;
import com.sebastian.levoria.network.DebugRenderingS2C;
import com.sebastian.levoria.network.HighlightBlockS2C;
import com.sebastian.levoria.network.ShakeScreenS2C;
import com.sebastian.levoria.network.TotemAnimationS2C;
import com.sebastian.levoria.network.specific.ApplyEditedDoorMatC2S;
import com.sebastian.levoria.network.specific.DoorMatEditRequestS2C;
import com.sebastian.levoria.util.Commands;
import com.sebastian.levoria.util.ModSounds;
import com.sebastian.levoria.world.MoonWorldEffects;
import com.sebastian.levoria.world.tree.ShadowTreeEffects;
import com.sebastian.levoria.world.tree.ShadowTreeTrunkPlacer;
import com.sebastian.levoria.world.gen.ModWorldGen;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
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

	//damage types
	public static final RegistryKey<DamageType> NO_OXYGEN_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id("no_oxygen"));

	@Override
	public void onInitialize() {

		//Important -> Register Custom Tree Trunk Placers First!
		Registry.register(Registries.TRUNK_PLACER_TYPE, Levoria.id("shadow_tree_trunk_placer"), SHADOW_TRUNK_PLACER);

		//Main Registration

		ModItems.registerModItems();
		ModDataComponentTypes.registerDataComponentTypes();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerModBlockEntities();
		ModEnchantmentEffects.registerEnchantmentEffects();
		ModItemGroups.registerModItemGrops();
		ModSounds.registerSounds();
		ModWorldGen.generateModWorldGen();


		//PACKETS
		PayloadTypeRegistry.playS2C().register(TotemAnimationS2C.ID, TotemAnimationS2C.PACKET_CODEC);
		PayloadTypeRegistry.playS2C().register(HighlightBlockS2C.ID, HighlightBlockS2C.PACKET_CODEC);
		PayloadTypeRegistry.playS2C().register(ShakeScreenS2C.ID, ShakeScreenS2C.PACKET_CODEC);
		PayloadTypeRegistry.playS2C().register(DebugRenderingS2C.ID, DebugRenderingS2C.PACKET_CODEC);
		PayloadTypeRegistry.playS2C().register(DoorMatEditRequestS2C.ID, DoorMatEditRequestS2C.PACKET_CODEC);
		PayloadTypeRegistry.playC2S().register(ApplyEditedDoorMatC2S.ID, ApplyEditedDoorMatC2S.PACKET_CODEC);

		ServerPlayNetworking.registerGlobalReceiver(ApplyEditedDoorMatC2S.ID, (payload, context) -> {
			context.server().execute(() -> {
				ServerWorld world = context.player().getServerWorld();
				if(world.getBlockEntity(payload.pos()) instanceof DoorMatBlockEntity be) {
					if (payload.str().isEmpty()) {
						be.setMessage("");
					}
					if(payload.str().length() == 1 || payload.str().length() == 2) {
						return;
					}
					be.setMessage(payload.str());
				}
			});
		});

		//PayloadTypeRegistry.playC2S().register(ConfigEditor.ConfigurationPayload.ID, ConfigEditor.ConfigurationPayload.PACKET_CODEC);

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

		// TREE-EFFECTS
		ShadowTreeEffects.registerIt();

		//COMMANDS
		Commands.registerCommands();

		//LOAD-CONFIG
		ConfigManager.registerConfigUn_Loaders();

		//WORLD-EFFECTS
		MoonWorldEffects.register();
	}



	public static Identifier id(String sub) {
		return Identifier.of(MOD_ID, sub);
	}
}