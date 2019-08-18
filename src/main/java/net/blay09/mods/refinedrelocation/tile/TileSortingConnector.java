package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.ModTiles;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileSortingConnector extends TileMod implements ITickable {

    private final ISortingGridMember sortingGridMember = Capabilities.getDefaultInstance(Capabilities.SORTING_GRID_MEMBER);

    public TileSortingConnector() {
        super(ModTiles.sortingConnector);
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
        return Capabilities.SORTING_GRID_MEMBER.orEmpty(cap, LazyOptional.of(() -> sortingGridMember));
    }

    @Override
    public void tick() {
        this.baseTick();
    }
}
