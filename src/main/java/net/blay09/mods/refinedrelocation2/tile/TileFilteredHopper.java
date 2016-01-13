package net.blay09.mods.refinedrelocation2.tile;

import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation2.api.filter.IFilter;
import net.minecraft.item.ItemStack;

public class TileFilteredHopper extends TileBetterHopper {

    private final IFilter rootFilter = RefinedRelocationAPI.createRootFilter();

    @Override
    public String getName() {
        return hasCustomName() ? customName : "container." + RefinedRelocation2.MOD_ID + ":filtered_hopper";
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        return rootFilter.passesFilter(itemStack);
    }

}
