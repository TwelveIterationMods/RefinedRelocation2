package net.blay09.mods.refinedrelocation.grid;

import com.google.common.collect.Lists;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.filter.ISimpleFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGrid;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.LinkedList;

public class SortingInventory extends SortingGridMember implements ISortingInventory {

    private final LinkedList<SortingStack> sortingStackList = Lists.newLinkedList();
    private LazyOptional<? extends ISimpleFilter> filter;
    private int priority;

    @Override
    public LazyOptional<IItemHandler> getItemHandler() {
        return getBlockEntity().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
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
    public LazyOptional<? extends ISimpleFilter> getFilter() {
        return filter;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        filter = getBlockEntity().getCapability(Capabilities.ROOT_FILTER);
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
                if (ItemStack.isSame(itemStack, sortingStack.getItemStack()) && ItemStack.isSameItemSameTags(itemStack, sortingStack.getItemStack())) {
                    RefinedRelocationAPI.insertIntoSortingGrid(this, sortingStack.getSlotIndex(), itemStack);
                }
                sortingGrid.setSortingActive(false);
            }
        }
    }

    @Override
    public void onSlotChanged(int slotIndex) {
        if (getSortingGrid() == null || getSortingGrid().isSortingActive() || isRemote()) {
            return;
        }

        LazyOptional<IItemHandler> itemHandlerCap = getItemHandler();
        itemHandlerCap.ifPresent(itemHandler -> {
            ItemStack itemStack = itemHandler.getStackInSlot(slotIndex);
            if (!itemStack.isEmpty()) {
                sortingStackList.add(new SortingStack(itemHandler, slotIndex, itemStack));
            }
        });
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag compound = new CompoundTag();
        compound.putShort("Priority", (short) priority);
        return compound;
    }

    @Override
    public void deserialize(CompoundTag compound) {
        priority = compound.getShort("Priority");
    }
}
