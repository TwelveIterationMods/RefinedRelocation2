package net.blay09.mods.refinedrelocation.block.entity;

import net.blay09.mods.balm.api.block.entity.BalmBlockEntity;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.refinedrelocation.block.FastHopperBlock;
import net.blay09.mods.refinedrelocation.menu.FastHopperMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FastHopperBlockEntity extends BalmBlockEntity implements BalmMenuProvider, Nameable {

    private static final Predicate<? super Entity> HAS_ITEM_HANDLER = (entity) -> entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent();

    private final ItemStackHandler itemHandler = createItemHandler();

    private Component customName;
    private int cooldown;

    public FastHopperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.fastHopper.get(), pos, state);
    }

    public FastHopperBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos ,state);
    }

    protected ItemStackHandler createItemHandler() {
        return new ItemStackHandler(5) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FastHopperBlockEntity blockEntity) {
        blockEntity.serverTick(level, pos, state);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (level != null) {
            cooldown--;
            if (cooldown <= 0) {
                boolean isEnabled = state.getValue(FastHopperBlock.ENABLED);
                if (!isEnabled) {
                    return;
                }

                Direction facing = state.getValue(FastHopperBlock.FACING);
                Direction opposite = facing.getOpposite();
                BlockEntity facingTile = level.getBlockEntity(worldPosition.relative(facing));
                LazyOptional<IItemHandler> targetItemHandlerCap = facingTile != null ? facingTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, opposite) : LazyOptional.empty();
                boolean hasSpace = false;
                if (targetItemHandlerCap.isPresent()) {
                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                        ItemStack itemStack = itemHandler.getStackInSlot(i);
                        if (!itemStack.isEmpty()) {
                            pushItem(i, targetItemHandlerCap.orElseThrow(ConcurrentModificationException::new));
                        }
                        if (!hasSpace && (itemStack.isEmpty() || itemStack.getCount() < itemStack.getMaxStackSize())) {
                            hasSpace = true;
                        }
                    }
                } else {
                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                        if (itemHandler.getStackInSlot(i).isEmpty()) {
                            hasSpace = true;
                            break;
                        }
                    }
                }

                if (hasSpace) {
                    LazyOptional<IItemHandler> itemHandlerCap = getItemHandlerAt(worldPosition.relative(Direction.UP));
                    if (itemHandlerCap.isPresent()) {
                        pullItem(itemHandlerCap.orElseThrow(ConcurrentModificationException::new));
                    }
                }

                cooldown = 20;
            }

            for (ItemEntity itemEntity : findItemsAbove()) {
                pullItem(itemEntity);
            }
        }
    }

    private LazyOptional<IItemHandler> getItemHandlerAt(BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity != null) {
            return blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN);
        }

        List<Entity> list = level.getEntities((Entity) null, new AABB(pos.getX() - 0.5, pos.getY() - 0.5, pos.getZ() - 0.5, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), EntitySelector.CONTAINER_ENTITY_SELECTOR);
        if (!list.isEmpty()) {
            Entity entity = list.get(level.random.nextInt(list.size()));
            return LazyOptional.of(() -> new InvWrapper(((Container) entity)));
        }

        return LazyOptional.empty();
    }

    private List<ItemEntity> findItemsAbove() {
        return Hopper.SUCK.toAabbs().stream().flatMap((aabb) -> level.getEntitiesOfClass(ItemEntity.class, aabb.move(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()), EntitySelector.ENTITY_STILL_ALIVE).stream()).collect(Collectors.toList());
    }

    private void pushItem(int sourceSlot, IItemHandler targetItemHandler) {
        ItemStack sourceStack = itemHandler.extractItem(sourceSlot, Items.AIR.getItemStackLimit(ItemStack.EMPTY), true);
        ItemStack restStack = ItemHandlerHelper.insertItem(targetItemHandler, sourceStack, false);
        itemHandler.extractItem(sourceSlot, restStack.isEmpty() ? sourceStack.getCount() : sourceStack.getCount() - restStack.getCount(), false);
    }

    private void pullItem(IItemHandler sourceItemHandler) {
        for (int i = 0; i < sourceItemHandler.getSlots(); i++) {
            ItemStack sourceStack = sourceItemHandler.extractItem(i, Items.AIR.getItemStackLimit(ItemStack.EMPTY), true);
            if (!sourceStack.isEmpty()) {
                ItemStack restStack = ItemHandlerHelper.insertItem(itemHandler, sourceStack, false);
                sourceItemHandler.extractItem(i, restStack.isEmpty() ? Items.AIR.getItemStackLimit(ItemStack.EMPTY) : sourceStack.getCount() - restStack.getCount(), false);
                break;
            }
        }
    }

    private boolean pullItem(ItemEntity entityItem) {
        ItemStack sourceStack = entityItem.getItem();
        ItemStack restStack = ItemHandlerHelper.insertItem(itemHandler, sourceStack, false);
        if (!restStack.isEmpty()) {
            entityItem.setItem(restStack);
        } else {
            entityItem.remove(Entity.RemovalReason.DISCARDED);
        }
        return restStack.isEmpty() || restStack.getCount() < sourceStack.getCount();
    }

    @Override
    public CompoundTag save(CompoundTag tagCompound) {
        super.save(tagCompound);
        tagCompound.put("ItemHandler", itemHandler.serializeNBT());
        if (customName != null) {
            tagCompound.putString("CustomName", customName.getString());
        }

        return tagCompound;
    }

    @Override
    public void load(CompoundTag compound) {
        itemHandler.deserializeNBT(compound.getCompound("ItemHandler"));
        customName = compound.contains("CustomName") ? new TextComponent(compound.getString("CustomName")) : null;
    }

    public String getUnlocalizedName() {
        return "container.refinedrelocation:fast_hopper";
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent(getUnlocalizedName());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        LazyOptional<T> result = super.getCapability(cap, side);
        if (!result.isPresent()) {
            result = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> itemHandler));
        }

        return result;
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
        return new FastHopperMenu(i, playerInventory, this);
    }

    @Override
    public Component getName() {
        return customName != null ? customName : new TranslatableComponent(getUnlocalizedName());
    }

    @Override
    public boolean hasCustomName() {
        return customName != null;
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return customName;
    }

    public void onEntityCollision(Entity entity) {
        if (entity instanceof ItemEntity) {
            if (Shapes.joinIsNotEmpty(Shapes.create(entity.getBoundingBox().move(-worldPosition.getX(), -worldPosition.getY(), -worldPosition.getZ())), Hopper.SUCK, BooleanOp.AND)) {
                pullItem((ItemEntity) entity);
            }
        }
    }
}
