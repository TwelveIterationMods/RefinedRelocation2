package net.blay09.mods.refinedrelocation.filter;

import com.google.common.collect.Lists;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.container.ContainerRootFilter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.List;

public class RootFilter implements IRootFilter {

    private final List<SubFilterWrapper> filterList = Lists.newArrayList();

    @Override
    public int getFilterCount() {
        return filterList.size();
    }

    @Override
    @Nullable
    public IFilter getFilter(int index) {
        if (index < 0 || index >= filterList.size()) {
            return null;
        }

        return filterList.get(index).getFilter();
    }

    @Override
    public boolean isBlacklist(int index) {
        return !(index < 0 || index >= filterList.size()) && filterList.get(index).isBlacklist();
    }

    @Override
    public void setIsBlacklist(int index, boolean isBlacklist) {
        if (index < 0 || index >= filterList.size()) {
            return;
        }
        filterList.get(index).setBlacklist(isBlacklist);
    }

    @Override
    public void addFilter(IFilter filter) {
        filterList.add(new SubFilterWrapper(filter));
    }

    @Override
    public void removeFilter(int index) {
        filterList.remove(index);
    }

    @Override
    public boolean passes(TileEntity tileEntity, ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }

        boolean passes = false;
        for (SubFilterWrapper filter : filterList) {
            boolean filterPasses = filter.getFilter().passes(tileEntity, itemStack);
            if (filterPasses && filter.isBlacklist()) {
                return false;
            }
            passes = passes || filterPasses;
        }

        return passes;
    }

    @Override
    public INBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        ListNBT filterList = new ListNBT();
        for (SubFilterWrapper filter : this.filterList) {
            CompoundNBT tagCompound = new CompoundNBT();
            filter.writeNBT(tagCompound);
            filterList.add(tagCompound);
        }
        compound.put("FilterList", filterList);
        return compound;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        filterList.clear();
        CompoundNBT compound = (CompoundNBT) nbt;
        ListNBT filterList = compound.getList("FilterList", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < filterList.size(); i++) {
            CompoundNBT tagCompound = filterList.getCompound(i);
            SubFilterWrapper filter = SubFilterWrapper.loadFromNBT(tagCompound);
            if (filter != null) {
                this.filterList.add(filter);
            }
        }
    }

    @Nullable
    @Override
    public IInteractionObject getConfiguration(PlayerEntity player, TileEntity tileEntity) {
        return new IInteractionObjectWithoutName() {
            @Override
            public Container createContainer(PlayerInventory inventoryPlayer, PlayerEntity entityPlayer) {
                return new ContainerRootFilter(player, tileEntity, LazyOptional.of(() -> RootFilter.this));
            }

            @Override
            public String getGuiID() {
                return "refinedrelocation:root_filter";
            }
        };
    }
}
