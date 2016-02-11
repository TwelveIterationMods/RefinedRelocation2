package net.blay09.mods.refinedrelocation2.capability;

import net.blay09.mods.refinedrelocation2.api.capability.ISortingInventory;
import net.blay09.mods.refinedrelocation2.api.filter.IFilter;
import net.minecraftforge.items.IItemHandler;

public class SortingInventoryDefaultImpl extends SortingGridMemberDefaultImpl implements ISortingInventory {

    private IItemHandler itemHandler;
    private IFilter filter;
    private int priority;

    @Override
    public void setItemHandler(IItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @Override
    public IItemHandler getItemHandler() {
        return itemHandler;
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
    public void setFilter(IFilter filter) {
        this.filter = filter;
    }

    @Override
    public IFilter getFilter() {
        return filter;
    }

    @Override
    public void slotChanged(int slotIndex) {

    }

}
