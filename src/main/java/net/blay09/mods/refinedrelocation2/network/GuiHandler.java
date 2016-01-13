package net.blay09.mods.refinedrelocation2.network;

import net.blay09.mods.refinedrelocation2.client.gui.GuiFilteredHopper;
import net.blay09.mods.refinedrelocation2.client.gui.GuiSortingChest;
import net.blay09.mods.refinedrelocation2.container.ContainerFilteredHopper;
import net.blay09.mods.refinedrelocation2.container.ContainerSortingChest;
import net.blay09.mods.refinedrelocation2.tile.TileBetterHopper;
import net.blay09.mods.refinedrelocation2.tile.TileSortingChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static final int GUI_SORTING_CHEST = 1;
    public static final int GUI_HOPPER = 2;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer entityPlayer, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tileEntity = world.getTileEntity(pos);
        switch(id) {
            case GUI_SORTING_CHEST:
                if(tileEntity instanceof TileSortingChest) {
                    return new ContainerSortingChest(entityPlayer, (TileSortingChest) tileEntity);
                }
                break;
            case GUI_HOPPER:
                if(tileEntity instanceof TileBetterHopper) {
                    return new ContainerFilteredHopper(entityPlayer, (TileBetterHopper) tileEntity);
                }
                break;
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer entityPlayer, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tileEntity = world.getTileEntity(pos);
        switch(id) {
            case GUI_SORTING_CHEST:
                if(tileEntity instanceof TileSortingChest) {
                    return new GuiSortingChest(entityPlayer, (TileSortingChest) tileEntity);
                }
                break;
            case GUI_HOPPER:
                if(tileEntity instanceof TileBetterHopper) {
                    return new GuiFilteredHopper(entityPlayer, (TileBetterHopper) tileEntity);
                }
                break;
        }
        return null;
    }

}
