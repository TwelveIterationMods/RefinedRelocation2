package net.blay09.mods.refinedrelocation.api;

import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.util.TileWrapper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

public interface InternalMethods {

	void registerFilter(String id, Class<? extends IFilter> filterClass);

	void addToSortingGrid(ISortingGridMember member);
	void removeFromSortingGrid(ISortingGridMember member);

	void insertIntoSortingGrid(ISortingInventory sortingInventory, int fromSlotIndex, ItemStack itemStack);

	GuiButton createOpenFilterButton(GuiContainer guiContainer, int buttonId);

	void openRootFilterGui(TileWrapper pos);

}
