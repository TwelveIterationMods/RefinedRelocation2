package net.blay09.mods.refinedrelocation2.api.gui;

import net.minecraft.util.BlockPos;

public interface IRootFilterGui {

    default boolean hasFilterButton() {
        return true;
    }
    BlockPos getBlockPos();

}
