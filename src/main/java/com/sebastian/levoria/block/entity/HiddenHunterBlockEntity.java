package com.sebastian.levoria.block.entity;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.block.HiddenHunterBlock;
import com.sebastian.levoria.util.ProjectileUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class HiddenHunterBlockEntity extends BlockEntity implements GeoBlockEntity {

    protected static final RawAnimation DEPLOY_ANIM = RawAnimation.begin().then("animation.hiddenhunter.attack", Animation.LoopType.PLAY_ONCE);
    public final AnimationController<HiddenHunterBlockEntity> CONTROLLER = new AnimationController<>(this, this::deployAnimController);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private boolean attacking = false;
    private int attacking_time_line = 0;
    private int detect_interval = 0;
    private boolean currentlyAttackingAPlayer = false;
    private List<UUID> entitiesToDelete = new ArrayList<>(); //Contains UUIDs

    public HiddenHunterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HIDDEN_HUNTER, pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(CONTROLLER);
    }

    public void setAttacking() {
        //triggerAnim(CONTROLLER.getName(), "animation.hiddenhunter.attack");
        //DEPLOY_ANIM.thenPlay("animation.hiddenhunter.attack");

        if(!currentlyAttackingAPlayer) {
            attacking = true;
            attacking_time_line = 0;
            currentlyAttackingAPlayer = true;
        }
        markDirty();
        world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 0);

    }

    public static void writeUUIDs(String groupName, List<UUID> uuids, NbtCompound parent) {
        NbtCompound tag = new NbtCompound();
        for (UUID uuid : uuids) {
            tag.putUuid(new StringBuilder("#").append(uuids.indexOf(uuid)).toString(), uuid);
        }
        parent.put(groupName, tag);
    }

    public static List<UUID> readUUIDs(String groupName, NbtCompound parent) {
        List<UUID> uuids = new ArrayList<>();
        if(!parent.contains(groupName)) {
            Levoria.LOGGER.warn("Could not read UUID-List due to parent not containing the group name.");
            Levoria.LOGGER.warn("Details:");
            Levoria.LOGGER.warn("Group-Name: {}", groupName);
            StringBuilder parentKeys = new StringBuilder();
            for (String key : parent.getKeys()) {
                parentKeys.append(key).append(", ");
            }
            parentKeys.replace(parentKeys.length() - 2, parentKeys.length(), "");
            Levoria.LOGGER.warn("Available Keys in Parent: {}", parentKeys.toString());
            return uuids;
        }

        NbtCompound child = (NbtCompound) parent.get(groupName);
        for (String key : child.getKeys()) {
           uuids.add(child.getUuid(key));
        }

        return uuids;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putBoolean("animation_attacking", attacking);
        nbt.putBoolean("currently_attacking", currentlyAttackingAPlayer);
        nbt.putInt("attacking_ticks", attacking_time_line);
        nbt.putInt("detect_interval", detect_interval);
        writeUUIDs("projectile_entity_uuids", entitiesToDelete, nbt);
        super.writeNbt(nbt, registries);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        if(nbt.contains("animation_attacking")) {
            attacking = nbt.getBoolean("animation_attacking");
        }
        if(nbt.contains("currently_attacking")) {
            currentlyAttackingAPlayer = nbt.getBoolean("currently_attacking");
        }
        if(nbt.contains("attacking_ticks")) {
            attacking_time_line = nbt.getInt("attacking_ticks");
        }
        if(nbt.contains("detect_interval")) {
            detect_interval = nbt.getInt("detect_interval");
        }
        entitiesToDelete = readUUIDs("projectile_entity_uuids", nbt);
        super.readNbt(nbt, registries);
    }

    protected <E extends HiddenHunterBlockEntity> PlayState deployAnimController(final AnimationState<E> state) {
        try {
            //System.out.println(attacking);
            if(attacking) {
                state.getController().forceAnimationReset();
                state.getController().setAnimation(RawAnimation.begin().thenPlay("animation.hiddenhunter.attack"));
                attacking = false;
            }
        } catch (Exception ignored) {}
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, HiddenHunterBlockEntity hiddenHunterBlockEntity) {
        if(world.isClient) return;
        if(hiddenHunterBlockEntity.currentlyAttackingAPlayer) {
            //TICK == EVENT (For this ex. tick 1 in anim play sound "shulker open"
            if(hiddenHunterBlockEntity.attacking_time_line == 1) {
                world.playSound(null, blockPos, SoundEvents.BLOCK_SHULKER_BOX_OPEN, SoundCategory.HOSTILE, isEvil(hiddenHunterBlockEntity) ? 0.1F : 1f, 1f);
            }

            if(hiddenHunterBlockEntity.attacking_time_line == 47) {
                world.playSound(null, blockPos, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.HOSTILE, isEvil(hiddenHunterBlockEntity) ? 0.1F : 1f, 1f);
                Direction direction = (Direction)world.getBlockState(blockPos).get(HiddenHunterBlock.FACING);
                int yaw = switch (direction) {
                    case NORTH -> 180;
                    case EAST -> -90;
                    case SOUTH -> 0;
                    case WEST -> 90;
                    case null, default -> {
                        Levoria.LOGGER.warn("Could not shoot error as Hidden Hunter, direction is invalid!");
                        yield 0;
                    }
                };

                Vec3d arrowDirection = switch (direction) {
                    case NORTH -> new Vec3d(0, 0, -0.8);
                    case EAST -> new Vec3d(0.8, 0, 0);
                    case SOUTH -> new Vec3d(0, 0, 0);
                    case WEST -> new Vec3d(-0.8, 0, 0.8);
                    case null, default -> {
                        Levoria.LOGGER.warn("Could not shoot arrow as Hidden Hunter, direction is invalid!");
                        yield new Vec3d(0, 0, 0);
                    }
                };

                Vec3d pos = hiddenHunterBlockEntity.getPos().toBottomCenterPos().add(arrowDirection).add(0,0.4,0);

                int arrowType = hiddenHunterBlockEntity.getRandomNumberUsingInts(1, 5); //Random int 1-5
                List<ArrowEntity> arrowEntities = world.getEntitiesByType(TypeFilter.equals(ArrowEntity.class), new Box(blockPos).expand(100.0), e -> hiddenHunterBlockEntity.entitiesToDelete.contains(e.getUuid()));
                for (ArrowEntity arrowEntity : arrowEntities) {
                    arrowEntity.kill((ServerWorld) arrowEntity.getWorld());
                }

                hiddenHunterBlockEntity.entitiesToDelete.clear();

                if(arrowType == 1) { //if 1 shoot normal arrow
                    hiddenHunterBlockEntity.entitiesToDelete.add(ProjectileUtils.summonArrowWithYaw((ServerWorld) hiddenHunterBlockEntity.world, pos, 2.0D, yaw, 5).getUuid());
                }
                if(arrowType == 2) { //if 2 shoot poison arrow
                    hiddenHunterBlockEntity.entitiesToDelete.add(ProjectileUtils.shootTippedArrow((ServerWorld) hiddenHunterBlockEntity.world, pos, 2.0f, yaw, 5, new StatusEffectInstance(StatusEffects.POISON, 80, 1)).getUuid()); //4 sec
                }
                if(arrowType == 3) { //if 3 shoot slowness arrow
                    hiddenHunterBlockEntity.entitiesToDelete.add(ProjectileUtils.shootTippedArrow((ServerWorld) hiddenHunterBlockEntity.world, pos, 2.0f, yaw, 5, new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 5)).getUuid()); //10 sec
                }
                if(arrowType == 4) { //if 4 shoot blindness arrow
                    hiddenHunterBlockEntity.entitiesToDelete.add(ProjectileUtils.shootTippedArrow((ServerWorld) hiddenHunterBlockEntity.world, pos, 2.0f, yaw, 5, new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 1)).getUuid()); // 5sec
                }
                if(arrowType == 5) { //if 5 shoot weakness arrow
                    hiddenHunterBlockEntity.entitiesToDelete.add(ProjectileUtils.shootTippedArrow((ServerWorld) hiddenHunterBlockEntity.world, pos, 2.0f, yaw, 5, new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 10)).getUuid()); //5sec
                }

                markDirty(world, blockPos, state);
            }

            if(hiddenHunterBlockEntity.attacking_time_line == 50) {
                world.playSound(null, blockPos, SoundEvents.BLOCK_SHULKER_BOX_CLOSE, SoundCategory.HOSTILE, isEvil(hiddenHunterBlockEntity) ? 0.1F : 1f, 1f);
                hiddenHunterBlockEntity.currentlyAttackingAPlayer = false;
                hiddenHunterBlockEntity.attacking_time_line = 0;
                hiddenHunterBlockEntity.markDirty();
            }
            hiddenHunterBlockEntity.attacking_time_line++;
        }

        if(isEvil(hiddenHunterBlockEntity)) {
            tryShoot(hiddenHunterBlockEntity);
        }
    }

    public static boolean isEvil(HiddenHunterBlockEntity be) {
        return be.world.getBlockState(be.pos).get(HiddenHunterBlock.EVIL, false);
    }

    public static List<PlayerEntity> findPlayersInRadius(World world, BlockPos pos, double radius) {
        Box area = new Box(pos).expand(radius);
        List<PlayerEntity> playersInRange = world.getEntitiesByClass(PlayerEntity.class, area, player -> true);
        return playersInRange;
    }

    public static boolean shouldAttack(List<PlayerEntity> players) {
        for (PlayerEntity player : players) {
            if(!player.isSpectator() && !player.isCreative()) {
                return true;
            }
        }
        return false;
    }

    public static List<PlayerEntity> findPlayersInFront(World world, BlockPos pos, double pos_infront, Direction direction) {
        List<PlayerEntity> allPlrs = new ArrayList<>();

        for (int i = 1; i < pos_infront; i++) {
            Box area = new Box(pos.offset(direction, i));
            for (PlayerEntity entitiesByClass : world.getEntitiesByClass(PlayerEntity.class, area, player -> true)) {
                allPlrs.add(entitiesByClass);
            }
        }

        return allPlrs;
    }

    public static void tryShoot(HiddenHunterBlockEntity be) {
        be.detect_interval++;
        if (be.detect_interval == 100) {
            List<PlayerEntity> infront = findPlayersInFront(be.world, be.pos, 10, be.world.getBlockState(be.pos).get(HiddenHunterBlock.FACING, Direction.DOWN));
            if(!infront.isEmpty() && shouldAttack(infront)) {
                be.setAttacking();
            }
            be.detect_interval = 0;
        }
    }

    public int getRandomNumberUsingInts(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }
}
