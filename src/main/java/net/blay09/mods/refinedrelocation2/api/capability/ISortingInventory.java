package net.blay09.mods.refinedrelocation2.api.capability;

import net.minecraftforge.items.IItemHandler;

public interface ISortingInventory extends ISortingGridMember, IFilterProvider {
    void setItemHandler(IItemHandler itemHandler);
    IItemHandler getItemHandler();
    int getPriority();
    void setPriority(int priority);
    void slotChanged(int slotIndex);
}
