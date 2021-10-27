package net.blay09.mods.refinedrelocation.filter;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.menu.RootFilterMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.Constants;

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
    public boolean passes(BlockEntity blockEntity, ItemStack itemStack, ItemStack originalStack) {
        if (itemStack.isEmpty()) {
            return false;
        }

        boolean passes = false;
        for (SubFilterWrapper filter : filterList) {
            boolean filterPasses = filter.getFilter().passes(blockEntity, itemStack, originalStack);
            if (filterPasses && filter.isBlacklist()) {
                return false;
            }
            passes = passes || filterPasses;
        }

        return passes;
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        ListTag filterList = new ListTag();
        for (SubFilterWrapper filter : this.filterList) {
            CompoundTag tagCompound = new CompoundTag();
            filter.writeNBT(tagCompound);
            filterList.add(tagCompound);
        }
        compound.put("FilterList", filterList);
        return compound;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        filterList.clear();
        CompoundTag compound = (CompoundTag) nbt;
        ListTag filterList = compound.getList("FilterList", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < filterList.size(); i++) {
            CompoundTag tagCompound = filterList.getCompound(i);
            SubFilterWrapper filter = SubFilterWrapper.loadFromNBT(tagCompound);
            if (filter != null) {
                this.filterList.add(filter);
            }
        }
    }

    @Nullable
    @Override
    public MenuProvider getConfiguration(Player player, BlockEntity blockEntity, int rootFilterIndex, int filterIndex) {
        return new BalmMenuProvider() {
            @Override
            public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
                return new RootFilterMenu(i, playerInventory, blockEntity, rootFilterIndex);
            }

            @Override
            public Component getDisplayName() {
                return new TranslatableComponent("refinedrelocation:root_filter");
            }

            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                buf.writeBlockPos(blockEntity.getBlockPos());
                buf.writeByte(rootFilterIndex);
            }
        };
    }
}
