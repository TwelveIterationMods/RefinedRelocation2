package net.blay09.mods.refinedrelocation2.api;

import net.blay09.mods.refinedrelocation2.api.capability.ISortingGridMember;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingInventory;
import net.blay09.mods.refinedrelocation2.api.filter.IFilter;
import net.blay09.mods.refinedrelocation2.api.grid.IWorldPos;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

public interface IInternalMethods {
    IFilter createRootFilter();
    ISortingGridMember createSortingMember(IWorldPos worldPos);
    ISortingInventory createSortingInventory(IWorldPos worldPos, IItemHandler itemHandler, boolean useRootFilter);
    void addToSortingGrid(ISortingGridMember sortingMember);
    void removeFromSortingGrid(ISortingGridMember sortingMember);
    void registerToolboxItem(Item item);

    @SideOnly(Side.CLIENT)
    GuiButton createOpenFilterButton(GuiContainer guiContainer);

    @SideOnly(Side.CLIENT)
    void openRootFilterGui(BlockPos blockPos);
}
