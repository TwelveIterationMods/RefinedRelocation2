package net.blay09.mods.refinedrelocation.api;

import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.util.TileWrapper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

public class RefinedRelocationAPI {

	private static InternalMethods internalMethods;

	public static void __internal__setupAPI(InternalMethods internalMethods) {
		RefinedRelocationAPI.internalMethods = internalMethods;
	}

	public static void addToSortingGrid(ISortingGridMember member) {
		internalMethods.addToSortingGrid(member);
	}

	public static void removeFromSortingGrid(ISortingGridMember member) {
		internalMethods.removeFromSortingGrid(member);
	}

	public static void registerFilter(String id, Class<? extends IFilter> filterClass) {
		internalMethods.registerFilter(id, filterClass);
	}

	public static GuiButton createOpenFilterButton(GuiContainer guiContainer, int buttonId) {
		return internalMethods.createOpenFilterButton(guiContainer, buttonId);
	}

	public static void openRootFilterGui(TileWrapper pos) {
		internalMethods.openRootFilterGui(pos);
	}

	public static void insertIntoSortingGrid(ISortingInventory sortingInventory, int fromSlotIndex, ItemStack itemStack) {
		internalMethods.insertIntoSortingGrid(sortingInventory, fromSlotIndex, itemStack);
	}

}
