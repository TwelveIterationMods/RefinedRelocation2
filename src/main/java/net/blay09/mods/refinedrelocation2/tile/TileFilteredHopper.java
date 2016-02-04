package net.blay09.mods.refinedrelocation2.tile;

import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation2.api.filter.IFilter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class TileFilteredHopper extends TileBetterHopper {

    private final IFilter rootFilter = RefinedRelocationAPI.createRootFilter();

    @Override
    protected ItemStackHandler createItemHandler() {
        return new ItemStackHandler(5) {
            @Override
            public ItemStack insertItem(int slot, ItemStack itemStack, boolean simulate) {
                if(itemStack == null || !rootFilter.passesFilter(itemStack)) {
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

}
