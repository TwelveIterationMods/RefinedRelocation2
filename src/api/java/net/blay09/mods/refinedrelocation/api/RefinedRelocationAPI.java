package net.blay09.mods.refinedrelocation.api;

import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

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

	public static GuiButton createOpenFilterButton(GuiContainer guiContainer, TileEntity tileEntity, int buttonId) {
		return internalMethods.createOpenFilterButton(guiContainer, tileEntity, buttonId);
	}

	public static void insertIntoSortingGrid(ISortingInventory sortingInventory, int fromSlotIndex, ItemStack itemStack) {
		internalMethods.insertIntoSortingGrid(sortingInventory, fromSlotIndex, itemStack);
	}

	public static void sendContainerMessageToServer(String key, String value) {
		internalMethods.sendContainerMessageToServer(key, value);
	}

	public static void sendContainerMessageToServer(String key, int value) {
		internalMethods.sendContainerMessageToServer(key, value);
	}

	public static void syncContainerValue(String key, String value, Iterable<IContainerListener> listeners) {
		internalMethods.syncContainerValue(key, value, listeners);
	}

	public static void syncContainerValue(String key, NBTTagCompound value, Iterable<IContainerListener> listeners) {
		internalMethods.syncContainerValue(key, value, listeners);
	}

	public static void registerGuiHandler(Class<? extends TileEntity> tileClass, ITileGuiHandler handler) {
		internalMethods.registerGuiHandler(tileClass, handler);
	}

}
