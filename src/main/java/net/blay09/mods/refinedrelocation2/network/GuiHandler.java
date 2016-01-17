package net.blay09.mods.refinedrelocation2.network;

import net.blay09.mods.refinedrelocation2.ModItems;
import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.api.capability.IRootFilterProvider;
import net.blay09.mods.refinedrelocation2.client.gui.GuiBetterHopper;
import net.blay09.mods.refinedrelocation2.client.gui.GuiFilter;
import net.blay09.mods.refinedrelocation2.client.gui.GuiSortingChest;
import net.blay09.mods.refinedrelocation2.client.gui.GuiToolbox;
import net.blay09.mods.refinedrelocation2.container.ContainerFilteredHopper;
import net.blay09.mods.refinedrelocation2.container.ContainerSortingChest;
import net.blay09.mods.refinedrelocation2.container.ContainerToolbox;
import net.blay09.mods.refinedrelocation2.tile.TileBetterHopper;
import net.blay09.mods.refinedrelocation2.tile.TileFilteredHopper;
import net.blay09.mods.refinedrelocation2.tile.TileSortingChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static final int GUI_SORTING_CHEST = 1;
    public static final int GUI_HOPPER = 2;
    public static final int GUI_TOOLBOX = 3;
    public static final int GUI_ROOTFILTER = 4;

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
            case GUI_TOOLBOX:
                int toolboxSlot = findToolboxSlot(entityPlayer);
                if(toolboxSlot != -1) {
                    entityPlayer.inventory.currentItem = toolboxSlot;
                    return new ContainerToolbox(entityPlayer, ModItems.toolbox.getInventory(entityPlayer));
                }
                break;
            case GUI_ROOTFILTER:
                if(tileEntity != null) {
                    IRootFilterProvider rootFilterProvider = tileEntity.getCapability(RefinedRelocation2.ROOT_FILTER_PROVIDER, null);
                    if (rootFilterProvider != null) {
                        return new ContainerFilteredHopper(entityPlayer, (TileBetterHopper) tileEntity);
                    }
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
                    return new GuiBetterHopper(entityPlayer, (TileBetterHopper) tileEntity, tileEntity instanceof TileFilteredHopper);
                }
                break;
            case GUI_TOOLBOX:
                int toolboxSlot = findToolboxSlot(entityPlayer);
                if(toolboxSlot != -1) {
                    entityPlayer.inventory.currentItem = toolboxSlot;
                    return new GuiToolbox(entityPlayer, ModItems.toolbox.getInventory(entityPlayer));
                }
                break;
            case GUI_ROOTFILTER:
                if(tileEntity != null) {
                    IRootFilterProvider rootFilterProvider = tileEntity.getCapability(RefinedRelocation2.ROOT_FILTER_PROVIDER, null);
                    if (rootFilterProvider != null) {
                        return new GuiFilter(entityPlayer);
                    }
                }
                break;
        }
        return null;
    }

    private int findToolboxSlot(EntityPlayer entityPlayer) {
        ItemStack itemStack = entityPlayer.getHeldItem();
        if(itemStack != null && itemStack.getItem() == ModItems.toolbox) {
            return entityPlayer.inventory.currentItem;
        }
        for(int i = 0; i < 9; i++) {
            ItemStack hotbarStack = entityPlayer.inventory.mainInventory[entityPlayer.inventory.mainInventory.length - 9 + i];
            if(hotbarStack != null && hotbarStack.getItem() == ModItems.toolbox) {
                return i;
            }
        }
        return -1;
    }

}
