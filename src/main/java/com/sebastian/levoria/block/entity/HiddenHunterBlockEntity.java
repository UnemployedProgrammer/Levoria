package com.sebastian.levoria.block.entity;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.block.HiddenHunterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
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
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HiddenHunterBlockEntity extends BlockEntity implements GeoBlockEntity {

    protected static final RawAnimation DEPLOY_ANIM = RawAnimation.begin().thenPlayAndHold("animation.hiddenhunter.attack");
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
            if(attacking ) {
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
                int pitch = switch (direction) {
                    case NORTH -> 180;
                    case EAST -> -90;
                    case SOUTH -> 0;
                    case WEST -> 90;
                    case null, default -> {
                        Levoria.LOGGER.warn("Could not shoot error as Hidden Hunter, direction is invalid!");
                        yield 0;
                    }
                };


            }

            if(hiddenHunterBlockEntity.attacking_time_line == 60) {
                world.playSound(null, blockPos, SoundEvents.BLOCK_SHULKER_BOX_OPEN, SoundCategory.HOSTILE,1f, 1f);
                hiddenHunterBlockEntity.currentlyAttackingAPlayer = false;
                hiddenHunterBlockEntity.attacking_time_line = 0;
                hiddenHunterBlockEntity.markDirty();
            }
            hiddenHunterBlockEntity.attacking_time_line++;
        }
    }

    //SHOOTING ARROW LOGIC

    public static Entity spawnProjectile(ProjectileItem item, ItemStack stack, HiddenHunterBlockEntity bE) {
        ProjectileItem.Settings settings = item.getProjectileSettings();
        settings.overrideDispenseEvent().ifPresent((dispenseEvent) -> {
            bE.world.syncWorldEvent(dispenseEvent, bE.getPos().up(), 0);
        });
        Direction direction = Direction.DOWN;
        ProjectileEntity projectileEntity = ProjectileEntity.spawnWithVelocity(item.createEntity(bE.world, new Position() {
            @Override
            public double getX() {
                return bE.getPos().getX();
            }

            @Override
            public double getY() {
                return bE.getPos().getY() + 1;
            }

            @Override
            public double getZ() {
                return bE.getPos().getZ();
            }
        }, stack, direction), ((ServerWorld) bE.world), stack, (double)direction.getOffsetX(), (double)direction.getOffsetY(), (double)direction.getOffsetZ(), settings.power(), settings.uncertainty());
        return projectileEntity;
    }
}
