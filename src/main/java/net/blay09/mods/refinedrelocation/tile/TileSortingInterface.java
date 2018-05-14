package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilitySimpleFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingGridMember;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingInventory;
import net.blay09.mods.refinedrelocation.util.RelativeSide;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileSortingInterface extends TileMod implements ITickable {

    private final ISortingInventory sortingInventory = Capabilities.getDefaultInstance(Capabilities.SORTING_INVENTORY);
    private final IRootFilter rootFilter = Capabilities.getDefaultInstance(Capabilities.ROOT_FILTER);

    private TileEntity cachedConnectedTile;
    private ItemStack[] lastInventory;
    private int currentDetectionSlot;

    public TileSortingInterface() {
    }

    @Override
    public void onLoad() {
        super.onLoad();
        sortingInventory.onLoad(this);
    }

    @Override
    public void update() {
        baseUpdate();

        if (cachedConnectedTile == null) {
            cachedConnectedTile = world.getTileEntity(pos.offset(getFacing()));
        } else if (cachedConnectedTile.isInvalid()) {
            cachedConnectedTile = null;
            lastInventory = null;
        }

        sortingInventory.onUpdate(this);

        if (!world.isRemote) {
            IItemHandler itemHandler = sortingInventory.getItemHandler();
            if (itemHandler != null) {
                int inventorySize = itemHandler.getSlots();

                // Create a copy of the target inventory so we can compare and detect changes
                if (lastInventory == null || inventorySize != lastInventory.length) {
                    lastInventory = new ItemStack[itemHandler.getSlots()];
                    for (int i = 0; i < inventorySize; i++) {
                        ItemStack currentStack = itemHandler.getStackInSlot(i);
                        lastInventory[i] = currentStack.isEmpty() ? ItemStack.EMPTY : currentStack.copy();
                    }
                    currentDetectionSlot = 0;
                }

                // Detect changes in the target inventory, nine slots at a time
                for (int j = 0; j < Math.min(9, inventorySize); j++) {
                    int i = currentDetectionSlot;
                    ItemStack prevStack = lastInventory[i];
                    ItemStack currentStack = itemHandler.getStackInSlot(i);
                    if (!ItemStack.areItemsEqual(prevStack, currentStack) || !ItemStack.areItemStackTagsEqual(prevStack, currentStack)) {
                        sortingInventory.onSlotChanged(i);
                        lastInventory[i] = currentStack.isEmpty() ? ItemStack.EMPTY : currentStack.copy();
                    }

                    currentDetectionSlot++;
                    if (currentDetectionSlot >= inventorySize) {
                        currentDetectionSlot = 0;
                    }
                }
            }
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        sortingInventory.onInvalidate(this);
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        sortingInventory.onInvalidate(this);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        sortingInventory.deserializeNBT(compound.getCompoundTag("SortingInventory"));
        rootFilter.deserializeNBT(compound.getCompoundTag("RootFilter"));
    }

    @Override
    public void readFromNBTSynced(NBTTagCompound compound) {
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("SortingInventory", sortingInventory.serializeNBT());
        compound.setTag("RootFilter", rootFilter.serializeNBT());
        return compound;
    }

    @Override
    public NBTTagCompound writeToNBTSynced(NBTTagCompound compound) {
        return compound;
    }

    @Override
    protected void onFirstTick() {
        cachedConnectedTile = world.getTileEntity(pos.offset(getFacing()));
    }

    public EnumFacing getFacing() {
        return EnumFacing.getFront(getBlockMetadata());
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return cachedConnectedTile != null && cachedConnectedTile.hasCapability(capability, getFacing().getOpposite());
        }

        return capability == CapabilitySortingInventory.CAPABILITY || capability == CapabilitySortingGridMember.CAPABILITY
                || capability == CapabilityRootFilter.CAPABILITY || capability == CapabilitySimpleFilter.CAPABILITY
                || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (cachedConnectedTile != null) {
                return (T) cachedConnectedTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getFacing().getOpposite());
            }
        } else if (capability == CapabilitySortingInventory.CAPABILITY || capability == CapabilitySortingGridMember.CAPABILITY) {
            return (T) sortingInventory;
        } else if (capability == CapabilityRootFilter.CAPABILITY || capability == CapabilitySimpleFilter.CAPABILITY) {
            return (T) rootFilter;
        }

        return null;
    }

    @Override
    public String getUnlocalizedName() {
        return "container.refinedrelocation:sorting_interface";
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

}
