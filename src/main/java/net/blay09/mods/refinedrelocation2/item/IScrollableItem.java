package net.blay09.mods.refinedrelocation2.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IScrollableItem {
    void onScrolled(EntityPlayer entityPlayer, ItemStack itemStack, int delta);
    void setScrollIndex(EntityPlayer entityPlayer, ItemStack itemStack, int index);
}
