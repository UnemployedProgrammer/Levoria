package com.sebastian.levoria.entity;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Set;

public class RocketEntity extends VehicleEntity implements GeoEntity {

    public static void teleportEntityToDimension(Entity entity, ServerWorld targetWorld) {
        if (entity.getWorld() instanceof ServerWorld) {
            entity.teleportTo(new TeleportTarget(targetWorld, entity.getBlockPos().withY(100).toCenterPos(), Vec3d.ZERO, 0.0F, 0.0F, false, false, Set.of(), new TeleportTarget.PostDimensionTransition() {
                @Override
                public void onTransition(Entity entity) {

                }
            }));
            /*
            // Detach passengers (must do this before teleporting)
            List<Entity> passengers = entity.getPassengerList();
            for (Entity passenger : passengers) {
                passenger.stopRiding();
            }

            // Teleport the entity to the target world
            entity.moveToWorld(targetWorld);
            entity.refreshPositionAndAngles(currentPos.x, currentPos.y, currentPos.z, yaw, pitch);

            // Reattach passengers in the new world
            for (Entity passenger : passengers) {
                passenger.moveToWorld(targetWorld);
                passenger.startRiding(entity, true); // Reattach with forced riding
            }
             */
        }
    }

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public RocketEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    private static final TrackedData<Boolean> FLIES =
            DataTracker.registerData(RocketEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SLOWED_DOWNFALL =
            DataTracker.registerData(RocketEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

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

    public void slowDownfall() {

    }

    @Override
    protected Item asItem() {
        return ModItems.MOON_BERRIES;
    }

    public Boolean getFlies() {
        return this.getF();
    }

    private boolean getF() {
        return this.dataTracker.get(FLIES);
    }

    private void setFlies(Boolean flies) {
        this.dataTracker.set(FLIES, flies);
    }


    private boolean isDownfallSlowed() {
        return this.dataTracker.get(SLOWED_DOWNFALL);
    }

    private void setSlowedDownfall(Boolean slowed) {
        this.dataTracker.set(SLOWED_DOWNFALL, slowed);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putBoolean("Flies", this.getF());
        nbt.putBoolean("SlowDownfall", this.isDownfallSlowed());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.dataTracker.set(SLOWED_DOWNFALL, nbt.getBoolean("SlowDownfall"));
    }

    @Override
    protected double getGravity() {
        return 0.4D;
    }

    @Override
    public boolean isCollidable() {
        return !getFlies();
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        ActionResult actionResult = super.interact(player, hand);
        if(!player.isSneaking()) {
            setFlies(true);
        }
        if (actionResult != ActionResult.PASS) {
            return actionResult;
        } else {
            return (ActionResult)(player.shouldCancelInteraction() || !this.getWorld().isClient && !player.startRiding(this)
                    ? ActionResult.PASS
                    : ActionResult.SUCCESS);
        }
    }

    protected float getPassengerHorizontalOffset() {
        return 0.0F;
    }

    protected double getPassengerAttachmentY(EntityDimensions dimensions) {
        return 1f;
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

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(FLIES, false);
        builder.add(SLOWED_DOWNFALL, false);
        super.initDataTracker(builder);
    }

    @Override
    public boolean collidesWith(Entity other) {
        return canCollide(this, other);
    }

    @Override
    public boolean canHit() {
        return !this.isRemoved();
    }

    public static boolean canCollide(Entity entity, Entity other) {
        return (other.isCollidable() || other.isPushable()) && !entity.isConnectedThroughVehicle(other);
    }

    @Override
    public Direction getMovementDirection() {
        return Direction.UP;
    }

    @Override
    public void tick() {
        super.tick();

        //if (!this.isOnGround() && !this.hasNoGravity()) {
        //            this.setVelocity(this.getVelocity().add(0, -0.04, 0)); // Gravity strength
        //        }
        //
        //        // Move the entity
        //        this.move(MovementType.SELF, this.getVelocity());
        //
        //        // Reduce horizontal movement (simulate friction/air drag)
        //        this.setVelocity(this.getVelocity().multiply(0.98, 1.0, 0.98));

        if(getFlies()) {
            this.setVelocity(this.getVelocity().add(0, 0.05, 0));
        } else {
            if(!this.isOnGround()) {
                this.setVelocity(this.getVelocity().add(0, isDownfallSlowed() ? -0.01 : -0.04, 0));
            } else {
                applyGravity();
            }
        }

        if(isDownfallSlowed()) {
            try {
                getFirstPassenger().fallDistance = 0.0F;
            } catch (Exception e) {
                Levoria.LOGGER.warn("Failed to slow down fall: {} ", e.getMessage());
            }
        }

        try {
            if(this.getY() > 300 && this.getFirstPassenger() instanceof PlayerEntity plr && getWorld() instanceof ServerWorld world) {
                setFlies(false);
                if(world.getServer().getOverworld().getRegistryKey().getValue().equals(this.getWorld().getRegistryKey().getValue())) {
                    //this.teleport(world.getServer().getWorld(RegistryKey.of(RegistryKeys.WORLD, Levoria.id("moon"))), plr.getX(), 300, plr.getZ(), Set.of(PositionFlag.X, PositionFlag.Y, PositionFlag.Z), plr.getYaw(), plr.getPitch(), false);
                    teleportEntityToDimension(this, world.getServer().getWorld(RegistryKey.of(RegistryKeys.WORLD, Levoria.id("moon"))));
                } else {
                    //this.teleport(world.getServer().getOverworld(), getX(), 300, getZ(), Set.of(PositionFlag.X, PositionFlag.Y, PositionFlag.Z), plr.getYaw(), plr.getPitch(), false);
                    teleportEntityToDimension(this, world.getServer().getOverworld());
                }
            }
        } catch (Exception e) {
            Levoria.LOGGER.info("Error while traveling to moon: {}", e.getMessage()); //execute in minecraft:overworld run tp @s -1933.67 -26.00 185.33 -49.18 3.75
        }

        this.move(MovementType.SELF, this.getVelocity());
    }

    //public static DefaultAttributeContainer.Builder createAttributes() {
    //        return MobEntity.createMobAttributes()
    //                .add(EntityAttributes.MAX_HEALTH, 1)
    //                .add(EntityAttributes.MOVEMENT_SPEED, 0.35)
    //                .add(EntityAttributes.ATTACK_DAMAGE, 1);
    //}

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
}
