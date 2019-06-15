package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.ModItems;
import net.blay09.mods.refinedrelocation.ModTiles;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.container.ContainerBlockExtender;
import net.blay09.mods.refinedrelocation.util.RelativeSide;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

public class TileBlockExtender extends TileMod implements ITickableTileEntity, IContainerProvider, IDroppableItemHandler {

    public TileBlockExtender() {
        super(ModTiles.blockExtender);
    }

    private class ItemHandlerWrapper implements IItemHandler {
        private final TileEntity tileEntity;
        private final Direction facing;
        private IItemHandler baseHandler;

        public ItemHandlerWrapper(TileEntity tileEntity, @Nullable Direction facing, IItemHandler baseHandler) {
            this.tileEntity = tileEntity;
            this.facing = facing;
            this.baseHandler = baseHandler;
        }

        public boolean revalidate() {
            LazyOptional<IItemHandler> itemHandlerCap = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
            itemHandlerCap.ifPresent(itemHandler -> baseHandler = itemHandler);
            return itemHandlerCap.isPresent();
        }

        @Override
        public int getSlots() {
            return baseHandler.getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return baseHandler.getStackInSlot(slot);
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            if (hasSlotLock) {
                if (stack.isEmpty() || getStackInSlot(slot).isEmpty()) {
                    return false;
                }
            }

            if (hasInputFilter) {
                //noinspection RedundantIfStatement
                if (stack.isEmpty() || !inputFilter.passes(tileEntity, stack)) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (!isItemValid(slot, stack)) {
                return stack;
            }

            if (hasStackLimiter) {
                int space = stackLimiterLimit - getStackInSlot(slot).getCount();
                if (space <= 0) {
                    return stack;
                }
                int amount = Math.min(stack.getCount(), space);
                if (amount < stack.getCount()) {
                    ItemStack insertStack = ItemHandlerHelper.copyStackWithSize(stack, amount);
                    ItemStack restStack = baseHandler.insertItem(slot, insertStack, simulate);
                    int initialRest = stack.getCount() - amount;
                    if (initialRest > 0) {
                        ItemStack otherRestStack = ItemHandlerHelper.copyStackWithSize(stack, initialRest);
                        if (restStack.isEmpty()) {
                            return otherRestStack;
                        }
                        if (ItemHandlerHelper.canItemStacksStack(stack, restStack)) {
                            restStack.grow(initialRest);
                        } else if (!world.isRemote) {
                            // If the remainder item is different than the input item upon failed insertion that's most likely a bug or bad game mechanic, so drop the other rest item in the world rather than having it disappear.
                            world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, otherRestStack));
                        }
                    }
                    return restStack;
                }
            }

            return baseHandler.insertItem(slot, stack, simulate);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (hasOutputFilter) {
                if (!outputFilter.passes(tileEntity, getStackInSlot(slot))) {
                    return ItemStack.EMPTY;
                }
            }

            return baseHandler.extractItem(slot, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return baseHandler.getSlots();
        }
    }

    private final ItemStackHandler itemHandlerUpgrades = new ItemStackHandler(3) {
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (stack.getItem() != ModItems.stackLimiter && stack.getItem() != ModItems.outputFilter && stack.getItem() != ModItems.inputFilter && stack.getItem() != ModItems.slotLock) { // TODO this is getting messy
                return stack;
            }

            for (int i = 0; i < getSlots(); i++) {
                if (getStackInSlot(i).getItem() == stack.getItem()) {
                    return stack;
                }
            }

            return super.insertItem(slot, stack, simulate);
        }

        @Override
        protected void onContentsChanged(int slot) {
            updateUpgrades();
            markDirty();
        }
    };

    private final Direction[] sideMappings = new Direction[5];
    private final IRootFilter inputFilter = Capabilities.getDefaultInstance(Capabilities.ROOT_FILTER);
    private final IRootFilter outputFilter = Capabilities.getDefaultInstance(Capabilities.ROOT_FILTER);
    private int stackLimiterLimit = 64;

    private boolean hasStackLimiter;
    private boolean hasSlotLock;
    private boolean hasInputFilter;
    private boolean hasOutputFilter;
    private TileEntity cachedConnectedTile;
    private final ItemHandlerWrapper[] cachedItemHandlers = new ItemHandlerWrapper[6];
    private final Direction[] cachedFacingToFacingMappings = new Direction[6];

    @Nullable
    public Direction getSideMapping(RelativeSide side) {
        return sideMappings[side.ordinal()];
    }

    public void setSideMapping(RelativeSide side, @Nullable Direction facing) {
        sideMappings[side.ordinal()] = facing;
        cachedFacingToFacingMappings[side.toFacing(getFacing()).ordinal()] = facing;
        markDirty();
    }

    @Override
    public void tick() {
        baseTick();

        if (cachedConnectedTile == null) {
            cachedConnectedTile = world.getTileEntity(pos.offset(getFacing()));
        } else if (cachedConnectedTile.isRemoved()) {
            cachedConnectedTile = null;
        }
    }

    @Override
    protected void onFirstTick() {
        cachedConnectedTile = world.getTileEntity(pos.offset(getFacing()));
        if (cachedConnectedTile instanceof TileBlockExtender) {
            cachedConnectedTile = null; // Disallow connecting block extenders to each other
        }
        for (int i = 0; i < sideMappings.length; i++) {
            cachedFacingToFacingMappings[RelativeSide.fromIndex(i).toFacing(getFacing()).ordinal()] = sideMappings[i];
        }
        updateUpgrades();
    }

    @Nullable
    public Direction getSideMapping(@Nullable Direction facing) {
        if (facing == null) {
            return getFacing().getOpposite();
        }
        return cachedFacingToFacingMappings[facing.ordinal()];
    }

    public Direction getFacing() {
        return getBlockState().get(BlockStateProperties.FACING);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);

        byte[] mappings = new byte[5];
        for (int i = 0; i < sideMappings.length; i++) {
            mappings[i] = sideMappings[i] == null ? -1 : (byte) sideMappings[i].getIndex();
        }

        compound.putByteArray("SideMappings", mappings);
        compound.put("Upgrades", itemHandlerUpgrades.serializeNBT());
        compound.putByte("StackLimiter", (byte) stackLimiterLimit);
        compound.put("InputFilter", inputFilter.serializeNBT());
        compound.put("OutputFilter", outputFilter.serializeNBT());
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);

        byte[] mappings = compound.getByteArray("SideMappings");
        if (mappings.length == 5) {
            for (int i = 0; i < mappings.length; i++) {
                if (mappings[i] != -1) {
                    sideMappings[i] = Direction.byIndex(mappings[i]);
                } else {
                    sideMappings[i] = null;
                }
            }
        }

        itemHandlerUpgrades.deserializeNBT(compound.getCompound("Upgrades"));
        stackLimiterLimit = compound.getByte("StackLimiter");
        inputFilter.deserializeNBT(compound.getCompound("InputFilter"));
        outputFilter.deserializeNBT(compound.getCompound("OutputFilter"));
        updateUpgrades();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cachedConnectedTile != null) {
            Direction ioSide = getSideMapping(side);
            if (ioSide != null) {
                if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && requiresItemHandlerWrapping()) {
                    int cacheIdx = ioSide.getIndex();
                    if (cachedItemHandlers[cacheIdx] != null) {
                        if (!cachedItemHandlers[cacheIdx].revalidate()) {
                            cachedItemHandlers[cacheIdx] = null;
                            return LazyOptional.empty();
                        }
                    } else {
                        LazyOptional<IItemHandler> itemHandlerCap = cachedConnectedTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, ioSide);
                        itemHandlerCap.ifPresent(itemHandler -> {
                            cachedItemHandlers[cacheIdx] = new ItemHandlerWrapper(cachedConnectedTile, ioSide, itemHandler);
                        });
                    }
                    return LazyOptional.of(() -> (T) cachedItemHandlers[cacheIdx]);
                }
                return cachedConnectedTile.getCapability(cap, ioSide);
            }
        }

        return super.getCapability(cap, side);
    }

    private boolean requiresItemHandlerWrapping() {
        return hasStackLimiter || hasSlotLock || hasInputFilter || hasOutputFilter;
    }

    @Override
    public String getUnlocalizedName() {
        return "container.refinedrelocation:block_extender";
    }

    public ItemStackHandler getItemHandlerUpgrades() {
        return itemHandlerUpgrades;
    }

    private void updateUpgrades() {
        hasStackLimiter = false;
        hasSlotLock = false;
        hasInputFilter = false;
        hasOutputFilter = false;
        for (int i = 0; i < itemHandlerUpgrades.getSlots(); i++) {
            ItemStack itemStack = itemHandlerUpgrades.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.getItem() == ModItems.stackLimiter) {
                    hasStackLimiter = true;
                } else if (itemStack.getItem() == ModItems.slotLock) {
                    hasSlotLock = true;
                } else if (itemStack.getItem() == ModItems.inputFilter) {
                    hasInputFilter = true;
                } else if (itemStack.getItem() == ModItems.outputFilter) {
                    hasOutputFilter = true;
                }
            }
        }
    }

    @Override
    public Collection<IItemHandler> getDroppedItemHandlers() {
        return Collections.singletonList(itemHandlerUpgrades);
    }

    public int getStackLimiterLimit() {
        return stackLimiterLimit;
    }

    public void setStackLimiterLimit(int stackLimiterLimit) {
        this.stackLimiterLimit = stackLimiterLimit;
    }

    public LazyOptional<IRootFilter> getInputFilter() {
        return LazyOptional.of(() -> inputFilter);
    }

    public LazyOptional<IRootFilter> getOutputFilter() {
        return LazyOptional.of(() -> outputFilter);
    }

    public boolean hasInputFilter() {
        return hasInputFilter;
    }

    public boolean hasOutputFilter() {
        return hasOutputFilter;
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ContainerBlockExtender(playerEntity, this);
    }

}
