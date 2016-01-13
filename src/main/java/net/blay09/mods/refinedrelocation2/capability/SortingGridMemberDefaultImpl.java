package net.blay09.mods.refinedrelocation2.capability;

import net.blay09.mods.refinedrelocation2.api.grid.IWorldPos;
import net.blay09.mods.refinedrelocation2.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingGridMember;
import net.blay09.mods.refinedrelocation2.api.grid.ISortingGrid;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class SortingGridMemberDefaultImpl implements ISortingGridMember {

    private boolean isInvalid;
    private IWorldPos worldPos;
    private ISortingGrid sortingGrid;

    @Override
    public boolean isInvalid() {
        return isInvalid;
    }

    @Override
    public void setWorldPos(IWorldPos worldPos) {
        this.worldPos = worldPos;
    }

    @Override
    public World getWorld() {
        return worldPos.getWorld();
    }

    @Override
    public BlockPos getPos() {
        return worldPos.getPos();
    }

    @Override
    public void setSortingGrid(ISortingGrid sortingGrid) {
        this.sortingGrid = sortingGrid;
    }

    @Override
    public ISortingGrid getSortingGrid() {
        return sortingGrid;
    }

    @Override
    public void onFirstTick() {
        RefinedRelocationAPI.addToSortingGrid(this);
    }

    @Override
    public void invalidate() {
        isInvalid = true;
        RefinedRelocationAPI.removeFromSortingGrid(this);
    }

}
