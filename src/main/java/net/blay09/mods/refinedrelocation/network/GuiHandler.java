package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.api.filter.IConfigurableFilter;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiChecklistFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiRootFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiSortingChest;
import net.blay09.mods.refinedrelocation.container.ContainerChecklistFilter;
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
	public static final int GUI_ANY_FILTER = 3;

	@Nullable
	public Container getContainer(int id, EntityPlayer player, MessageOpenGui message) {
		TileEntity actualTile = message.hasPosition() ? player.worldObj.getTileEntity(message.getPos()) : null;
		TileOrMultipart tileEntity = actualTile != null ? new TileWrapper(actualTile) : null;
		switch(id) {
			case GUI_SORTING_CHEST:
				return actualTile instanceof TileSortingChest ? new ContainerSortingChest(player, (TileSortingChest) actualTile) : null;
			case GUI_ROOT_FILTER:
				return tileEntity != null ? (tileEntity.hasCapability(CapabilityRootFilter.CAPABILITY, null) ? new ContainerRootFilter(player, tileEntity) : null) : null;
			case GUI_ANY_FILTER:
				if(tileEntity != null) {
					IRootFilter rootFilter = tileEntity.getCapability(CapabilityRootFilter.CAPABILITY, null);
					if (rootFilter != null) {
						IFilter filter = rootFilter.getFilter(message.getIntValue());
						if(filter instanceof IConfigurableFilter) {
							return ((IConfigurableFilter) filter).createContainer(player, tileEntity);
						} else if(filter instanceof IChecklistFilter) {
							return new ContainerChecklistFilter(player, tileEntity, (IChecklistFilter) filter);
						}
					}
				}
				break;
		}
		return null;
	}

	@Nullable
	@SideOnly(Side.CLIENT)
	public GuiScreen getGuiScreen(int id, EntityPlayer player, MessageOpenGui message) {
		TileEntity actualTile = message.hasPosition() ? player.worldObj.getTileEntity(message.getPos()) : null;
		TileOrMultipart tileEntity = actualTile != null ? new TileWrapper(actualTile) : null;
		switch(id) {
			case GUI_SORTING_CHEST:
				return actualTile instanceof TileSortingChest ? new GuiSortingChest(player, (TileSortingChest) actualTile) : null;
			case GUI_ROOT_FILTER:
				return tileEntity != null ? (tileEntity.hasCapability(CapabilityRootFilter.CAPABILITY, null) ? new GuiRootFilter(player, tileEntity) : null) : null;
			case GUI_ANY_FILTER:
				if(tileEntity != null) {
					Container container = player.openContainer;
					if (container instanceof ContainerRootFilter) {
						IFilter filter = ((ContainerRootFilter) container).getRootFilter().getFilter(message.getIntValue());
						if (filter instanceof IConfigurableFilter) {
							return ((IConfigurableFilter) filter).createGuiScreen(player, tileEntity);
						} else if(filter instanceof IChecklistFilter) {
							return new GuiChecklistFilter(player, tileEntity, (IChecklistFilter) filter);
						}
					}
				}
				break;
		}
		return null;
	}

}
