package com.sebastian.levoria.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class RocketEntity extends VehicleEntity {
    public RocketEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (this.isRemoved()) {
            return true;
        } else if(this.isAlwaysInvulnerableTo(source)) {
            return false;
        }

        if(source.getAttacker() instanceof PlayerEntity plr) {
            if(plr.isSneaking()) {
                this.emitGameEvent(GameEvent.ENTITY_DAMAGE, source.getAttacker());
                this.killAndDropSelf(world, source);
            }
        } else {
            this.killAndDropSelf(world, source);
        }
        return super.damage(world, source, amount);
    }

    @Override
    protected Item asItem() {
        return null;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    protected float getPassengerHorizontalOffset() {
        return 0.0F;
    }

    protected double getPassengerAttachmentY(EntityDimensions dimensions) {
        return 0;
    }

    @Override
    protected Vec3d getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        float f = this.getPassengerHorizontalOffset();
        if (this.getPassengerList().size() > 1) {
            int i = this.getPassengerList().indexOf(passenger);
            if (i == 0) {
                f = 0.2F;
            } else {
                f = -0.6F;
            }

            if (passenger instanceof AnimalEntity) {
                f += 0.2F;
            }
        }

        return new Vec3d(0.0, this.getPassengerAttachmentY(dimensions), (double)f).rotateY(-this.getYaw() * (float) (Math.PI / 180.0));
    }
}
