package net.blay09.mods.refinedrelocation2.api.filter;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public interface IFilter {

    default boolean isAvailableForTile(TileEntity tileEntity) {
        return true;
    }
    String getTypeName();
    boolean passesFilter(ItemStack itemStack);
    boolean isBlacklist();
    void setBlacklist(boolean isBlacklist);

}
