package net.blay09.mods.refinedrelocation2.api.capability;

import net.blay09.mods.refinedrelocation2.api.filter.IFilter;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public interface ISortingInventory extends ISortingGridMember {
    void setItemHandler(IItemHandler itemHandler);
    IItemHandler getItemHandler();
    void setFilter(IFilter filter);
    IFilter getFilter();
    void slotChanged(int slotIndex);
}
