package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.ModTileEntities;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileSortingConnector extends TileMod implements ITickableTileEntity {

    private final ISortingGridMember sortingGridMember = Capabilities.getDefaultInstance(Capabilities.SORTING_GRID_MEMBER);

    public TileSortingConnector() {
        super(ModTileEntities.sortingConnector);
    }

    @Override
    public void onFirstTick() {
        super.onFirstTick();
        sortingGridMember.onFirstTick(this);
    }

    @Override
    public void onChunkUnloaded() {
        sortingGridMember.onInvalidate(this);
    }

    @Override
    public void remove() {
        super.remove();
        sortingGridMember.onInvalidate(this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        LazyOptional<T> result = super.getCapability(cap, side);
        if (!result.isPresent()) {
            result = Capabilities.SORTING_GRID_MEMBER.orEmpty(cap, LazyOptional.of(() -> sortingGridMember));
        }

        return result;
    }

    @Override
    public void tick() {
        this.baseTick();
    }
}
