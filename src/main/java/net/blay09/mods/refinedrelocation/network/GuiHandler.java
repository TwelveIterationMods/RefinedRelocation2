package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.client.gui.GuiSortingChest;
import net.blay09.mods.refinedrelocation.container.ContainerSortingChest;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class GuiHandler {

	public static final int GUI_SORTING_CHEST = 1;
	public static final int GUI_ROOT_FILTER = 2;

	@Nullable
	public Container getContainer(int id, EntityPlayer player, MessageOpenGui message) {
		TileEntity tileEntity = player.worldObj.getTileEntity(message.getPos());
		switch(id) {
			case GUI_SORTING_CHEST:
				return tileEntity instanceof TileSortingChest ? new ContainerSortingChest(player, (TileSortingChest) tileEntity) : null;
		}
		return null;
	}

	@Nullable
	@SideOnly(Side.CLIENT)
	public GuiScreen getGuiScreen(int id, EntityPlayer player, MessageOpenGui message) {
		TileEntity tileEntity = player.worldObj.getTileEntity(message.getPos());
		switch(id) {
			case GUI_SORTING_CHEST:
				return tileEntity instanceof TileSortingChest ? new GuiSortingChest(player, (TileSortingChest) tileEntity) : null;
		}
		return null;
	}

}
