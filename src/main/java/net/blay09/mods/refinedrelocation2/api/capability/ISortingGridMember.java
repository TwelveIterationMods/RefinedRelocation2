package net.blay09.mods.refinedrelocation2.api.capability;

import net.blay09.mods.refinedrelocation2.api.grid.IWorldPos;
import net.blay09.mods.refinedrelocation2.api.grid.ISortingGrid;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface ISortingGridMember {

    boolean isInvalid();
    void setWorldPos(IWorldPos worldPos);
    World getWorld();
    BlockPos getPos();

    void setSortingGrid(ISortingGrid sortingGrid);
    ISortingGrid getSortingGrid();

    void onFirstTick();
    void invalidate();

}
