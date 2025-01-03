package com.sebastian.levoria.item;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.network.HighlightBlockS2C;
import com.sebastian.levoria.network.TotemAnimationS2C;
import com.sebastian.levoria.tags.ModTags;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DowsingRod extends Item {

    List<Block> oreBlocks = new ArrayList<>();

    public DowsingRod(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        try {

            //SETUP HASHMAP
            if(oreBlocks.isEmpty()) {
                oreBlocks.add(Blocks.COAL_ORE);
                oreBlocks.add(Blocks.DEEPSLATE_COAL_ORE);

                oreBlocks.add(Blocks.IRON_ORE);
                oreBlocks.add(Blocks.DEEPSLATE_IRON_ORE);

                oreBlocks.add(Blocks.GOLD_ORE);
                oreBlocks.add(Blocks.DEEPSLATE_GOLD_ORE);

                oreBlocks.add(Blocks.LAPIS_ORE);
                oreBlocks.add(Blocks.DEEPSLATE_LAPIS_ORE);

                oreBlocks.add(Blocks.REDSTONE_ORE);
                oreBlocks.add(Blocks.DEEPSLATE_REDSTONE_ORE);

                oreBlocks.add(Blocks.DIAMOND_ORE);
                oreBlocks.add(Blocks.DEEPSLATE_DIAMOND_ORE);

                oreBlocks.add(Blocks.NETHER_QUARTZ_ORE);

                oreBlocks.add(Blocks.NETHER_GOLD_ORE);

                oreBlocks.add(Blocks.ANCIENT_DEBRIS);
            }

            if(user.isSneaking()) {
                Integer next = user.getStackInHand(hand).set(ModDataComponentTypes.LOOKFOR_ORE, nextIndex(user, hand));
                user.sendMessage(Text.literal("» ").append(oreBlocks.get(next).getName()).append(" «").formatted(Formatting.GREEN), true);
                ServerPlayNetworking.send(user.getServer().getPlayerManager().getPlayer(user.getUuid()), new TotemAnimationS2C(TotemAnimationS2C.toId(oreBlocks.get(next))));
            } else {

                //Survival Logic

                if(user.getStackInHand(Hand.OFF_HAND).isOf(Items.STICK)) {
                    user.getStackInHand(hand).setDamage(user.getStackInHand(hand).getDamage() - 1);
                    user.getStackInHand(Hand.OFF_HAND).decrement(1);
                    return ActionResult.CONSUME;
                }

                user.getItemCooldownManager().set(user.getStackInHand(hand), 50);
                user.getStackInHand(hand).damage(1, user);
                if(user.getStackInHand(hand).getDamage() == user.getStackInHand(hand).getMaxDamage()) {
                    user.setStackInHand(hand, ItemStack.EMPTY);
                    world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1f, 1f);
                }

                //Logic

                Integer current = user.getStackInHand(hand).get(ModDataComponentTypes.LOOKFOR_ORE);

                System.out.println(current - 1);

                if(current - 1 == -1)
                    current = 15;

                if(current == null)
                    current = 15;


                for (BlockPos blockPos : getSphereAroundPlayer(user, 10)) {
                    //System.out.println("Found at " + blockPos.toString() + " block: " + world.getBlockState(blockPos).getBlock().getName());
                    if (world.getBlockState(blockPos).isOf(oreBlocks.get(current - 1))) {
                        //System.out.println("Found!");
                        ServerPlayNetworking.send(user.getServer().getPlayerManager().getPlayer(user.getUuid()), new HighlightBlockS2C(blockPos, 160));
                    }
                }

            }

        } catch (Exception e) {
            //user.sendMessage(Text.literal("JAVA ERROR!").formatted(Formatting.RED), false);
            //            user.sendMessage(Text.literal(e.getMessage()).formatted(Formatting.RED), false);
            //Levoria.LOGGER.error(e.getMessage());
        }

        //user.getStackInHand(hand).set(ModDataComponentTypes.LOOKFOR_ORE, Blocks.DIAMOND_BLOCK);
        return ActionResult.SUCCESS;
    }

    //TOOLTIP

    /*
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {

        try {
            if(oreBlocks.isEmpty()) {
                oreBlocks.add(Blocks.COAL_ORE);
                oreBlocks.add(Blocks.DEEPSLATE_COAL_ORE);

                oreBlocks.add(Blocks.IRON_ORE);
                oreBlocks.add(Blocks.DEEPSLATE_IRON_ORE);

                oreBlocks.add(Blocks.GOLD_ORE);
                oreBlocks.add(Blocks.DEEPSLATE_GOLD_ORE);

                oreBlocks.add(Blocks.LAPIS_ORE);
                oreBlocks.add(Blocks.DEEPSLATE_LAPIS_ORE);

                oreBlocks.add(Blocks.REDSTONE_ORE);
                oreBlocks.add(Blocks.DEEPSLATE_REDSTONE_ORE);

                oreBlocks.add(Blocks.DIAMOND_ORE);
                oreBlocks.add(Blocks.DEEPSLATE_DIAMOND_ORE);

                oreBlocks.add(Blocks.NETHER_QUARTZ_ORE);

                oreBlocks.add(Blocks.NETHER_GOLD_ORE);

                oreBlocks.add(Blocks.ANCIENT_DEBRIS);
            }

            Integer current = stack.get(ModDataComponentTypes.LOOKFOR_ORE);
            if (current == null) current = 0;

            tooltip.add(Text.literal("» ").append(oreBlocks.get(current - 1).getName()).append(" «").formatted(Formatting.GREEN));
        } catch (Exception e) {
            tooltip.add(Text.literal("» ").append(Text.translatable("item.levoria.dowsing_rod.tooltip.error")).append(" «").formatted(Formatting.RED));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
     */

    //ENCHANT


    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        //return enchantment.matchesId(Enchantments.UNBREAKING.getValue()) || enchantment.matchesId(Enchantments.MENDING.getValue());
        return super.canBeEnchantedWith(stack, enchantment, context);
    }

    //UTIL
    public static List<BlockPos> getSphereAroundPlayer(PlayerEntity player, int radius) {
        List<BlockPos> positions = new ArrayList<>();
        BlockPos playerPos = player.getBlockPos(); // Get the player's block position

        int radiusSquared = radius * radius;

        // Iterate through a cube containing the sphere
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos offset = new BlockPos(playerPos.getX() + x, playerPos.getY() + y, playerPos.getZ() + z);

                    // Check if the current block is within the sphere radius
                    if (x * x + y * y + z * z <= radiusSquared) {
                        positions.add(offset);
                    }
                }
            }
        }

        return positions;
    }

    private Integer nextIndex(PlayerEntity user, Hand hand) {
        Integer current = user.getStackInHand(hand).get(ModDataComponentTypes.LOOKFOR_ORE);
        if(current == null) {
            user.getStackInHand(hand).set(ModDataComponentTypes.LOOKFOR_ORE, 0);
            current = 0;
        }
        Integer next = current + 1;
        if(next >= oreBlocks.size()) {
            next = 0;
        }

        return next;
    }
}
