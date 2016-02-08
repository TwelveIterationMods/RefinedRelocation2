package net.blay09.mods.refinedrelocation2.tile;

import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingInventory;
import net.blay09.mods.refinedrelocation2.api.grid.IWorldPos;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemStackHandler;

public class TileFilteredHopper extends TileBetterHopper implements IWorldPos {

    private final ISortingInventory sortingInventory;

    public TileFilteredHopper() {
        sortingInventory = RefinedRelocationAPI.createSortingInventory(this, itemHandler, true);
    }

    @Override
    protected ItemStackHandler createItemHandler() {
        return new ItemStackHandler(5) {
            @Override
            public ItemStack insertItem(int slot, ItemStack itemStack, boolean simulate) {
                if(itemStack == null || !sortingInventory.getFilter().passesFilter(itemStack)) {
                    return itemStack;
                }
                return super.insertItem(slot, itemStack, simulate);
            }
        };
    }

    @Override
    public String getName() {
        return hasCustomName() ? customName : "container." + RefinedRelocation2.MOD_ID + ":filtered_hopper";
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == RefinedRelocation2.SORTING_INVENTORY) {
            return (T) sortingInventory;
        }
        return super.getCapability(capability, facing);
    }
}
