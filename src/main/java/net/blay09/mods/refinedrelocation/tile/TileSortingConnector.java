package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileSortingConnector extends TileEntity {

    private final ISortingGridMember sortingGridMember = Capabilities.getDefaultInstance(Capabilities.SORTING_GRID_MEMBER);

    public TileSortingConnector() {
    }

    public TileSortingConnector(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        sortingGridMember.onLoad(this);
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
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
        return Capabilities.SORTING_GRID_MEMBER.orEmpty(cap, LazyOptional.of(() -> sortingGridMember));
    }

}
