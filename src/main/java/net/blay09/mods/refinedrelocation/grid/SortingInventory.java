package net.blay09.mods.refinedrelocation.grid;

import com.google.common.collect.Lists;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.filter.ISimpleFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGrid;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.LinkedList;

public class SortingInventory extends SortingGridMember implements ISortingInventory {

    private final LinkedList<SortingStack> sortingStackList = Lists.newLinkedList();
    private ISimpleFilter filter;
    private int priority;

    @Override
    @Nullable
    public IItemHandler getItemHandler() {
        return getTileEntity().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public ISimpleFilter getFilter() {
        return filter;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        filter = getTileEntity().getCapability(CapabilityRootFilter.CAPABILITY, null);
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();
        if (!sortingStackList.isEmpty()) {
            SortingStack sortingStack = sortingStackList.removeFirst();
            ISortingGrid sortingGrid = getSortingGrid();
            if (sortingGrid != null) {
                sortingGrid.setSortingActive(true);
                ItemStack itemStack = sortingStack.getItemHandler().getStackInSlot(sortingStack.getSlotIndex());
                if (ItemStack.areItemStacksEqual(itemStack, sortingStack.getItemStack()) && ItemStack.areItemStackTagsEqual(itemStack, sortingStack.getItemStack())) {
                    RefinedRelocationAPI.insertIntoSortingGrid(this, sortingStack.getSlotIndex(), itemStack);
                }
                sortingGrid.setSortingActive(false);
            }
        }
    }

    @Override
    public void onSlotChanged(int slotIndex) {
        if (getSortingGrid() == null || getSortingGrid().isSortingActive() || getTileEntity().getWorld().isRemote) {
            return;
        }

        IItemHandler itemHandler = getItemHandler();
        if (itemHandler == null) {
            return;
        }

        ItemStack itemStack = itemHandler.getStackInSlot(slotIndex);
        if (itemStack.isEmpty()) {
            return;
        }

        sortingStackList.add(new SortingStack(itemHandler, slotIndex, itemStack));
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setShort("Priority", (short) priority);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        priority = compound.getShort("Priority");
    }
}
