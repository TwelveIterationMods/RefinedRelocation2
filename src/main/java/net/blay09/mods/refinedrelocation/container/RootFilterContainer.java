package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.Priority;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.container.IContainerMessage;
import net.blay09.mods.refinedrelocation.api.container.ReturnCallback;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingInventory;
import net.blay09.mods.refinedrelocation.filter.FilterRegistry;
import net.blay09.mods.refinedrelocation.filter.RootFilter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class RootFilterContainer extends FilterContainer {

    public static final String KEY_ROOT_FILTER = "RootFilter";
    public static final String KEY_ADD_FILTER = "AddFilter";
    public static final String KEY_EDIT_FILTER = "EditFilter";
    public static final String KEY_DELETE_FILTER = "DeleteFilter";
    public static final String KEY_PRIORITY = "Priority";
    public static final String KEY_BLACKLIST = "Blacklist";
    public static final String KEY_BLACKLIST_INDEX = "FilterIndex";

    private final PlayerEntity entityPlayer;
    private final TileEntity tileEntity;
    private final IRootFilter rootFilter;

    private ReturnCallback returnCallback;
    private ISortingInventory sortingInventory;

    private int lastFilterCount = -1;
    private int lastPriority;
    private final boolean[] lastBlacklist = new boolean[3];

    public RootFilterContainer(int windowId, PlayerInventory playerInventory, TileEntity tileEntity) {
        this(windowId, playerInventory, tileEntity, tileEntity.getCapability(CapabilityRootFilter.CAPABILITY));
    }

    public RootFilterContainer(int windowId, PlayerInventory playerInventory, TileEntity tileEntity, LazyOptional<IRootFilter> rootFilter) {
        super(ModContainers.rootFilter, windowId);

        this.entityPlayer = playerInventory.player;
        this.tileEntity = tileEntity;
        this.rootFilter = rootFilter.orElseGet(RootFilter::new);
        sortingInventory = tileEntity.getCapability(CapabilitySortingInventory.CAPABILITY)
                .orElseGet(() -> Capabilities.getDefaultInstance(CapabilitySortingInventory.CAPABILITY));

        addPlayerInventory(playerInventory, hasSortingInventory() ? 128 : 84);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        if (rootFilter.getFilterCount() != lastFilterCount) {
            syncFilterList();
            RefinedRelocationAPI.updateFilterPreview(entityPlayer, tileEntity, rootFilter);
        }

        for (int i = 0; i < lastBlacklist.length; i++) {
            boolean nowBlacklist = rootFilter.isBlacklist(i);
            if (lastBlacklist[i] != nowBlacklist) {
                CompoundNBT compound = new CompoundNBT();
                compound.putInt(KEY_BLACKLIST_INDEX, i);
                compound.putBoolean(KEY_BLACKLIST, nowBlacklist);
                RefinedRelocationAPI.syncContainerValue(KEY_BLACKLIST, compound, listeners);
                lastBlacklist[i] = nowBlacklist;
            }
        }

        if (sortingInventory.getPriority() != lastPriority) {
            RefinedRelocationAPI.syncContainerValue(KEY_PRIORITY, sortingInventory.getPriority(), listeners);
            lastPriority = sortingInventory.getPriority();
        }
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        ItemStack itemStack = super.slotClick(slotId, dragType, clickTypeIn, player);
        RefinedRelocationAPI.updateFilterPreview(player, tileEntity, rootFilter);
        return itemStack;
    }

    private void syncFilterList() {
        CompoundNBT tagCompound = new CompoundNBT();
        tagCompound.put(KEY_ROOT_FILTER, rootFilter.serializeNBT());
        RefinedRelocationAPI.syncContainerValue(KEY_ROOT_FILTER, tagCompound, listeners);
        lastFilterCount = rootFilter.getFilterCount();
        for (int i = 0; i < lastBlacklist.length; i++) {
            lastBlacklist[i] = rootFilter.isBlacklist(i);
        }
    }

    public TileEntity getTileEntity() {
        return tileEntity;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();

            if (index < 27) {
                if (!mergeItemStack(slotStack, 27, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(slotStack, 0, 27, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemStack;
    }

    @Override
    public void receivedMessageServer(IContainerMessage message) {
        switch (message.getKey()) {
            case KEY_ADD_FILTER: {
                String typeId = message.getStringValue();
                IFilter filter = FilterRegistry.createFilter(typeId);
                if (filter == null) {
                    // Client tried to create a filter that doesn't exist. Bad client!
                    return;
                }

                if (rootFilter.getFilterCount() >= 3) {
                    // Client tried to create more than three filters. Bad client!
                    return;
                }

                rootFilter.addFilter(filter);
                tileEntity.markDirty();
                lastFilterCount = rootFilter.getFilterCount();
                syncFilterList();
                RefinedRelocationAPI.updateFilterPreview(entityPlayer, tileEntity, rootFilter);
                INamedContainerProvider filterConfig = filter.getConfiguration(entityPlayer, tileEntity);
                if (filterConfig != null) {
                    NetworkHooks.openGui((ServerPlayerEntity) entityPlayer, filterConfig, it -> {
                        it.writeBlockPos(tileEntity.getPos());
                        it.writeInt(rootFilter.getFilterCount() - 1);
                    });
                }
                break;
            }
            case KEY_EDIT_FILTER: {
                int index = message.getIntValue();
                if (index < 0 || index >= rootFilter.getFilterCount()) {
                    // Client tried to edit a filter that doesn't exist. Bad client!
                    return;
                }
                IFilter filter = rootFilter.getFilter(index);
                if (filter != null) {
                    INamedContainerProvider filterConfig = filter.getConfiguration(entityPlayer, tileEntity);
                    if (filterConfig != null) {
                        NetworkHooks.openGui((ServerPlayerEntity) entityPlayer, filterConfig, it -> {
                            it.writeBlockPos(tileEntity.getPos());
                            it.writeInt(index);
                        });
                    }
                }
                break;
            }
            case KEY_DELETE_FILTER: {
                int index = message.getIntValue();
                if (index < 0 || index >= rootFilter.getFilterCount()) {
                    // Client tried to delete a filter that doesn't exist. Bad client!
                    return;
                }
                rootFilter.removeFilter(index);
                tileEntity.markDirty();
                break;
            }
            case KEY_PRIORITY:
                int value = message.getIntValue();
                if (value < Priority.LOWEST || value > Priority.HIGHEST) {
                    // Client tried to set an invalid priority. Bad client!
                    return;
                }

                sortingInventory.setPriority(value);
                tileEntity.markDirty();
                break;
            case KEY_BLACKLIST: {
                CompoundNBT tagCompound = message.getNBTValue();
                int index = tagCompound.getInt(KEY_BLACKLIST_INDEX);
                if (index < 0 || index >= rootFilter.getFilterCount()) {
                    // Client tried to delete a filter that doesn't exist. Bad client!
                    return;
                }
                rootFilter.setIsBlacklist(index, tagCompound.getBoolean(KEY_BLACKLIST));
                tileEntity.markDirty();
                RefinedRelocationAPI.updateFilterPreview(entityPlayer, tileEntity, rootFilter);
                break;
            }
        }
    }

    @Override
    public void receivedMessageClient(IContainerMessage message) {
        switch (message.getKey()) {
            case KEY_ROOT_FILTER:
                rootFilter.deserializeNBT(message.getNBTValue().getCompound(KEY_ROOT_FILTER));
                break;
            case KEY_PRIORITY:
                sortingInventory.setPriority(message.getIntValue());
                break;
            case KEY_BLACKLIST:
                CompoundNBT compound = message.getNBTValue();
                rootFilter.setIsBlacklist(compound.getInt(KEY_BLACKLIST_INDEX), compound.getBoolean(KEY_BLACKLIST));
                break;
        }
    }

    public IRootFilter getRootFilter() {
        return rootFilter;
    }

    public boolean hasSortingInventory() {
        return tileEntity.getCapability(CapabilitySortingInventory.CAPABILITY).isPresent();
    }

    public ISortingInventory getSortingInventory() {
        return sortingInventory;
    }

    @Nullable
    public ReturnCallback getReturnCallback() {
        return returnCallback;
    }

    public RootFilterContainer setReturnCallback(@Nullable ReturnCallback returnCallback) {
        this.returnCallback = returnCallback;
        return this;
    }
}
