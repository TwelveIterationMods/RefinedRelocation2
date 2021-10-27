package net.blay09.mods.refinedrelocation.block.entity;

import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.filter.RootFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FilteredHopperBlockEntity extends FastHopperBlockEntity {
    private final IRootFilter rootFilter = new RootFilter();

    public FilteredHopperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.filteredHopper.get(), pos, state);
    }

    @Override
    protected ItemStackHandler createItemHandler() {
        return new ItemStackHandler(5) {
            @Override
            public ItemStack insertItem(int slot, ItemStack itemStack, boolean simulate) {
                if (itemStack.isEmpty() || !rootFilter.passes(FilteredHopperBlockEntity.this, itemStack, itemStack)) {
                    return itemStack;
                }
                return super.insertItem(slot, itemStack, simulate);
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
    public CompoundTag save(CompoundTag tagCompound) {
        super.save(tagCompound);
        tagCompound.put("RootFilter", rootFilter.serializeNBT());
        return tagCompound;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        rootFilter.deserializeNBT(compound.getCompound("RootFilter"));
    }

}
