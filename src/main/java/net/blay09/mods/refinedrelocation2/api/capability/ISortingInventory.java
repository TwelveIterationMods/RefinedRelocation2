package net.blay09.mods.refinedrelocation2.api.capability;

import net.blay09.mods.refinedrelocation2.api.filter.IFilter;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface ISortingInventory extends ISortingGridMember {
    void setInventory(IInventory inventory);
    void setFilter(IFilter filter);
    IFilter getFilter();
    boolean putInInventory(ItemStack itemStack, boolean simulate);
    void slotChanged(int slotIndex);
}
