package net.blay09.mods.refinedrelocation.api;

import mcmultipart.multipart.Multipart;
import net.blay09.mods.refinedrelocation.api.container.ITileGuiHandler;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.filter.ISimpleFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
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

	public static void registerFilter(Class<? extends IFilter> filterClass) {
		internalMethods.registerFilter(filterClass);
	}

	public static GuiButton createOpenFilterButton(GuiContainer guiContainer, TileEntity tileEntity, int buttonId) {
		return internalMethods.createOpenFilterButton(guiContainer, tileEntity, buttonId);
	}

	public static GuiButton createOpenFilterButton(GuiContainer guiContainer, Multipart part, int buttonId) {
		return internalMethods.createOpenFilterButton(guiContainer, part, buttonId);
	}

	public static void insertIntoSortingGrid(ISortingInventory sortingInventory, int fromSlotIndex, ItemStack itemStack) {
		internalMethods.insertIntoSortingGrid(sortingInventory, fromSlotIndex, itemStack);
	}

	public static void sendContainerMessageToServer(String key, String value) {
		internalMethods.sendContainerMessageToServer(key, value);
	}

	public static void sendContainerMessageToServer(String key, NBTTagCompound value) {
		internalMethods.sendContainerMessageToServer(key, value);
	}

	public static void sendContainerMessageToServer(String key, int value) {
		internalMethods.sendContainerMessageToServer(key, value);
	}

	public static void syncContainerValue(String key, String value, Iterable<IContainerListener> listeners) {
		internalMethods.syncContainerValue(key, value, listeners);
	}

	public static void syncContainerValue(String key, int value, Iterable<IContainerListener> listeners) {
		internalMethods.syncContainerValue(key, value, listeners);
	}

	public static void syncContainerValue(String key, byte[] value, Iterable<IContainerListener> listeners) {
		internalMethods.syncContainerValue(key, value, listeners);
	}

	public static void syncContainerValue(String key, NBTTagCompound value, Iterable<IContainerListener> listeners) {
		internalMethods.syncContainerValue(key, value, listeners);
	}

	public static void updateFilterPreview(EntityPlayer player, TileOrMultipart tileEntity, ISimpleFilter filter) {
		internalMethods.updateFilterPreview(player, tileEntity, filter);
	}

	public static void registerGuiHandler(Class clazz, ITileGuiHandler handler) {
		internalMethods.registerGuiHandler(clazz, handler);
	}

	public static void openRootFilterGui(EntityPlayer player, TileOrMultipart tileEntity) {
		if(tileEntity.isMultipart()) {
			openRootFilterGui(player, tileEntity.getMultipart());
		}  else {
			openRootFilterGui(player, tileEntity.getTileEntity());
		}
	}

	public static void openRootFilterGui(EntityPlayer player, TileEntity tileEntity) {
		internalMethods.openRootFilterGui(player, tileEntity);
	}

	public static void openRootFilterGui(EntityPlayer player, Multipart part) {
		internalMethods.openRootFilterGui(player, part);
	}

}
