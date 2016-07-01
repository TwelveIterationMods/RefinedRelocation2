package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiNameFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiRootFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiSortingChest;
import net.blay09.mods.refinedrelocation.container.ContainerNameFilter;
import net.blay09.mods.refinedrelocation.container.ContainerRootFilter;
import net.blay09.mods.refinedrelocation.container.ContainerSortingChest;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.blay09.mods.refinedrelocation.util.TileWrapper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class GuiHandler {

	public static final int GUI_SORTING_CHEST = 1;
	public static final int GUI_ROOT_FILTER = 2;
	public static final int GUI_NAME_FILTER = 3;

	@Nullable
	public Container getContainer(int id, EntityPlayer player, MessageOpenGui message) {
		TileEntity tileEntity = message.hasPosition() ? player.worldObj.getTileEntity(message.getPos()) : null;
		switch(id) {
			case GUI_SORTING_CHEST:
				return tileEntity instanceof TileSortingChest ? new ContainerSortingChest(player, (TileSortingChest) tileEntity) : null;
			case GUI_ROOT_FILTER:
				return tileEntity != null ? (tileEntity.hasCapability(CapabilityRootFilter.CAPABILITY, null) ? new ContainerRootFilter(player, new TileWrapper(tileEntity)) : null) : null;
			case GUI_NAME_FILTER:
				return tileEntity != null ? (tileEntity.hasCapability(CapabilityRootFilter.CAPABILITY, null) ? new ContainerNameFilter(player, new TileWrapper(tileEntity), message.getIntValue()) : null) : null;
		}
		return null;
	}

	@Nullable
	@SideOnly(Side.CLIENT)
	public GuiScreen getGuiScreen(int id, EntityPlayer player, MessageOpenGui message) {
		TileEntity tileEntity = message.hasPosition() ? player.worldObj.getTileEntity(message.getPos()) : null;
		switch(id) {
			case GUI_SORTING_CHEST:
				return tileEntity instanceof TileSortingChest ? new GuiSortingChest(player, (TileSortingChest) tileEntity) : null;
			case GUI_ROOT_FILTER:
				return tileEntity != null ? (tileEntity.hasCapability(CapabilityRootFilter.CAPABILITY, null) ? new GuiRootFilter(player, new TileWrapper(tileEntity)) : null) : null;
			case GUI_NAME_FILTER:
				return tileEntity != null ? (tileEntity.hasCapability(CapabilityRootFilter.CAPABILITY, null) ? new GuiNameFilter(player, new TileWrapper(tileEntity), message.getIntValue()) : null) : null;
		}
		return null;
	}

}
