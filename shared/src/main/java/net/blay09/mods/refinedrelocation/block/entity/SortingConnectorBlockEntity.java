package net.blay09.mods.refinedrelocation.block.entity;

import net.blay09.mods.balm.api.block.entity.BalmBlockEntity;
import net.blay09.mods.balm.api.block.entity.OnLoadHandler;
import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.blay09.mods.refinedrelocation.grid.SortingGridMember;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SortingConnectorBlockEntity extends BalmBlockEntity implements OnLoadHandler {

    private final ISortingGridMember sortingGridMember = new SortingGridMember();

    public SortingConnectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.sortingConnector.get(), pos, state);
    }

    @Override
    public void onLoad() {
        sortingGridMember.onFirstTick(this);
    }

    @Override
    public void onChunkUnloaded() {
        sortingGridMember.onInvalidate(this);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
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

}
