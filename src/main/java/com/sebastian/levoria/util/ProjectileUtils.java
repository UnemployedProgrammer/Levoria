package com.sebastian.levoria.util;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class ProjectileUtils {
    /**
     * Summons an arrow with velocity based on yaw and optional pitch.
     *
     * @param world    The ServerWorld to spawn the arrow in.
     * @param position The starting position of the arrow.
     * @param speed    The speed of the arrow.
     * @param yaw      The horizontal angle in degrees (0 = positive Z-axis).
     * @param pitch    The vertical angle in degrees (0 = horizontal, optional).
     */
    public static ArrowEntity summonArrowWithYaw(ServerWorld world, Vec3d position, double speed, float yaw, float pitch) {
        // Convert yaw and pitch from degrees to radians
        double yawRadians = Math.toRadians(yaw);
        double pitchRadians = Math.toRadians(pitch);

        // Calculate velocity components
        double velocityX = -speed * Math.sin(yawRadians) * Math.cos(pitchRadians);
        double velocityY = speed * Math.sin(pitchRadians);
        double velocityZ = speed * Math.cos(yawRadians) * Math.cos(pitchRadians);

        // Create the arrow entity
        ArrowEntity arrow = new ArrowEntity(EntityType.ARROW, world);

        // Set the position of the arrow
        arrow.setPos(position.x, position.y, position.z);

        // Set the velocity of the arrow
        arrow.setVelocity(new Vec3d(velocityX, velocityY, velocityZ));

        // Set the yaw and pitch of the arrow
        arrow.setYaw(yaw);
        arrow.setPitch(pitch);

        // Spawn the arrow in the world
        world.spawnEntity(arrow);
        return arrow;
    }

    /**
     * Shoots a tipped arrow with specified velocity, yaw, and pitch.
     *
     * @param world    The ServerWorld to spawn the arrow in.
     * @param position The starting position of the arrow.
     * @param speed    The speed of the arrow.
     * @param yaw      The horizontal angle in degrees (0 = positive Z-axis).
     * @param pitch    The vertical angle in degrees (0 = horizontal).
     */
    public static ArrowEntity shootTippedArrow(ServerWorld world, Vec3d position, float speed, float yaw, float pitch, StatusEffectInstance poisonEffect) {
        // Create the arrow entity
        ArrowEntity arrowEntity = new ArrowEntity(EntityType.ARROW, world);
        arrowEntity.setPos(position.x, position.y, position.z);

        // Add Poison effect to the arrow
        arrowEntity.addEffect(poisonEffect);

        // Calculate velocity components
        double yawRadians = Math.toRadians(yaw);
        double pitchRadians = Math.toRadians(pitch);
        double velocityX = -speed * Math.sin(yawRadians) * Math.cos(pitchRadians);
        double velocityY = speed * Math.sin(pitchRadians);
        double velocityZ = speed * Math.cos(yawRadians) * Math.cos(pitchRadians);

        // Set the velocity of the arrow
        arrowEntity.setVelocity(velocityX, velocityY, velocityZ, speed, 0);

        // Set the rotation of the arrow
        arrowEntity.setYaw(yaw);
        arrowEntity.setPitch(pitch);

        // Spawn the arrow in the world
        world.spawnEntity(arrowEntity);
        return arrowEntity;
    }
}
