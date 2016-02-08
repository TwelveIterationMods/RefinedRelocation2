package net.blay09.mods.refinedrelocation2.api;

import net.blay09.mods.refinedrelocation2.api.capability.ISortingGridMember;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingInventory;
import net.blay09.mods.refinedrelocation2.api.filter.IFilter;
import net.blay09.mods.refinedrelocation2.api.grid.IWorldPos;
import net.blay09.mods.refinedrelocation2.client.gui.element.GuiButtonEditFilter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

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

    public static ISortingInventory createSortingInventory(IWorldPos worldPos, IItemHandler itemHandler, boolean useRootFilter) {
        return internalMethods.createSortingInventory(worldPos, itemHandler, useRootFilter);
    }

    @SideOnly(Side.CLIENT)
    public static GuiButton createOpenFilterButton(GuiContainer guiContainer) {
        return internalMethods.createOpenFilterButton(guiContainer);
    }

    @SideOnly(Side.CLIENT)
    public static void openRootFilterGui(BlockPos blockPos) {
        internalMethods.openRootFilterGui(blockPos);
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
