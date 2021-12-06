package net.blay09.mods.refinedrelocation.block.entity;

import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.filter.RootFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FilteredHopperBlockEntity extends FastHopperBlockEntity {
    private final IRootFilter rootFilter = new RootFilter();

    public FilteredHopperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.filteredHopper.get(), pos, state);
    }

    @Override
    protected Container createItemHandler() {
        return new DefaultContainer(5) {
            @Override
            public boolean canPlaceItem(int slot, ItemStack itemStack) {
                return !itemStack.isEmpty() && rootFilter.passes(FilteredHopperBlockEntity.this, itemStack, itemStack);
            }
        };
    }

    @Override
    public String getUnlocalizedName() {
        return "container.refinedrelocation:filtered_hopper";
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        LazyOptional<T> result = super.getCapability(cap, side);
        if (!result.isPresent()) {
            result = Capabilities.ROOT_FILTER.orEmpty(cap, LazyOptional.of(() -> rootFilter));
        }

        if (!result.isPresent()) {
            result = Capabilities.SIMPLE_FILTER.orEmpty(cap, LazyOptional.of(() -> rootFilter));
        }

        return result;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.put("RootFilter", rootFilter.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        rootFilter.deserializeNBT(tag.getCompound("RootFilter"));
    }

}
