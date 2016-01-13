package net.blay09.mods.refinedrelocation2.api;

import net.blay09.mods.refinedrelocation2.api.capability.ISortingGridMember;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingInventory;
import net.blay09.mods.refinedrelocation2.api.filter.IFilter;
import net.blay09.mods.refinedrelocation2.api.grid.IWorldPos;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;

public class RefinedRelocationAPI {

    private static IInternalMethods internalMethods;

    public static void setupAPI(IInternalMethods internalMethods) {
        if(RefinedRelocationAPI.internalMethods != null) {
            throw new RuntimeException("Refined Relocation 2 API has already been initialized");
        }
        RefinedRelocationAPI.internalMethods = internalMethods;
    }

    public static IFilter createRootFilter() {
        return internalMethods.createRootFilter();
    }

    public static ISortingGridMember createSortingMember(IWorldPos worldPos) {
        return internalMethods.createSortingMember(worldPos);
    }

    public static ISortingInventory createSortingInventory(IWorldPos worldPos, IInventory inventory, boolean useRootFilter) {
        return internalMethods.createSortingInventory(worldPos, inventory, useRootFilter);
    }

    public static void addToSortingGrid(ISortingGridMember sortingMember) {
        internalMethods.addToSortingGrid(sortingMember);
    }

    public static void removeFromSortingGrid(ISortingGridMember sortingMember) {
        internalMethods.removeFromSortingGrid(sortingMember);
    }

    public static void registerToolboxItem(Item item) {
        internalMethods.registerToolboxItem(item);
    }
}
