package net.blay09.mods.refinedrelocation2.api;

import net.blay09.mods.refinedrelocation2.api.capability.ISortingGridMember;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingInventory;
import net.blay09.mods.refinedrelocation2.api.filter.IFilter;
import net.blay09.mods.refinedrelocation2.api.grid.IWorldPos;
import net.minecraft.inventory.IInventory;

public interface IInternalMethods {
    IFilter createRootFilter();
    ISortingGridMember createSortingMember(IWorldPos worldPos);
    ISortingInventory createSortingInventory(IWorldPos worldPos, IInventory inventory, boolean useRootFilter);
    void addToSortingGrid(ISortingGridMember sortingMember);
    void removeFromSortingGrid(ISortingGridMember sortingMember);
}
