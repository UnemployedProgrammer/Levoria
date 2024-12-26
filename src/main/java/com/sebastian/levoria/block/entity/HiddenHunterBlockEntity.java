package com.sebastian.levoria.block.entity;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.block.HiddenHunterBlock;
import com.sebastian.levoria.util.ProjectileUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Random;

public class HiddenHunterBlockEntity extends BlockEntity implements GeoBlockEntity {

    protected static final RawAnimation DEPLOY_ANIM = RawAnimation.begin().then("animation.hiddenhunter.attack", Animation.LoopType.PLAY_ONCE);
    public final AnimationController<HiddenHunterBlockEntity> CONTROLLER = new AnimationController<>(this, this::deployAnimController);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private boolean attacking = false;
    private int attacking_time_line = 0;
    private boolean currentlyAttackingAPlayer = false;

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

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putBoolean("animation_attacking", attacking);
        nbt.putBoolean("currently_attacking", currentlyAttackingAPlayer);
        nbt.putInt("attacking_ticks", attacking_time_line);
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
                world.playSound(null, blockPos, SoundEvents.BLOCK_SHULKER_BOX_OPEN, SoundCategory.HOSTILE,1f, 1f);
            }

            if(hiddenHunterBlockEntity.attacking_time_line == 47) {
                world.playSound(null, blockPos, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.HOSTILE,1f, 1f);
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
                //System.out.println(arrowType);

                if(arrowType == 1) { //if 1 shoot normal arrow
                    ProjectileUtils.summonArrowWithYaw((ServerWorld) hiddenHunterBlockEntity.world, pos, 2.0D, yaw, 5);
                }
                if(arrowType == 2) { //if 2 shoot poison arrow
                    ProjectileUtils.shootTippedArrow((ServerWorld) hiddenHunterBlockEntity.world, pos, 2.0f, yaw, 5, new StatusEffectInstance(StatusEffects.POISON, 80, 1)); //4 sec
                }
                if(arrowType == 3) { //if 3 shoot slowness arrow
                    ProjectileUtils.shootTippedArrow((ServerWorld) hiddenHunterBlockEntity.world, pos, 2.0f, yaw, 5, new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 5)); //10 sec
                }
                if(arrowType == 4) { //if 4 shoot blindness arrow
                    ProjectileUtils.shootTippedArrow((ServerWorld) hiddenHunterBlockEntity.world, pos, 2.0f, yaw, 5, new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 1)); // 5sec
                }
                if(arrowType == 5) { //if 5 shoot weakness arrow
                    ProjectileUtils.shootTippedArrow((ServerWorld) hiddenHunterBlockEntity.world, pos, 2.0f, yaw, 5, new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 10)); //5sec
                }
            }

            if(hiddenHunterBlockEntity.attacking_time_line == 50) {
                world.playSound(null, blockPos, SoundEvents.BLOCK_SHULKER_BOX_CLOSE, SoundCategory.HOSTILE,1f, 1f);
                hiddenHunterBlockEntity.currentlyAttackingAPlayer = false;
                hiddenHunterBlockEntity.attacking_time_line = 0;
                hiddenHunterBlockEntity.markDirty();
            }
            hiddenHunterBlockEntity.attacking_time_line++;
        }
    }

    public int getRandomNumberUsingInts(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }
}
