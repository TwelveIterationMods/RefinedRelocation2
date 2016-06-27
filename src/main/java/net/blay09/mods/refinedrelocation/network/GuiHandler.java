package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.client.gui.GuiSortingChest;
import net.blay09.mods.refinedrelocation.container.ContainerSortingChest;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {
	public static final int GUI_SORTING_CHEST = 1;
	public static final int GUI_ROOT_FILTER = 2;

	@Override
	@Nullable
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tileEntity = world.getTileEntity(pos);
		switch(id) {
			case GUI_SORTING_CHEST:
				return tileEntity instanceof TileSortingChest ? new ContainerSortingChest(player, (TileSortingChest) tileEntity) : null;
		}
		return null;
	}

	@Override
	@Nullable
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tileEntity = world.getTileEntity(pos);
		switch(id) {
			case GUI_SORTING_CHEST:
				return tileEntity instanceof TileSortingChest ? new GuiSortingChest(player, (TileSortingChest) tileEntity) : null;
		}
		return null;
	}
}
