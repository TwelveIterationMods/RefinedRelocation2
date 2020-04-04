package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.ModTileEntities;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FilteredHopperTileEntity extends FastHopperTileEntity {
    private final IRootFilter rootFilter = Capabilities.getDefaultInstance(Capabilities.ROOT_FILTER);

    public FilteredHopperTileEntity() {
        super(ModTileEntities.filteredHopper);
    }

    @Override
    protected ItemStackHandler createItemHandler() {
        return new ItemStackHandler(5) {
            @Override
            public ItemStack insertItem(int slot, ItemStack itemStack, boolean simulate) {
                if (itemStack.isEmpty() || !rootFilter.passes(FilteredHopperTileEntity.this, itemStack)) {
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
    public CompoundNBT write(CompoundNBT tagCompound) {
        super.write(tagCompound);
        tagCompound.put("RootFilter", rootFilter.serializeNBT());
        return tagCompound;
    }

    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
        rootFilter.deserializeNBT(tagCompound.getCompound("RootFilter"));
    }

}
