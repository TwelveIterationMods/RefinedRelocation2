package net.blay09.mods.refinedrelocation2.balyware;

import net.minecraft.item.ItemStack;

public class ItemUtils {

    public static boolean canMergeItems(ItemStack first, ItemStack second) {
        return first.getItem() == second.getItem() && (first.getMetadata() == second.getMetadata() && ItemStack.areItemStackTagsEqual(first, second));
    }

}
