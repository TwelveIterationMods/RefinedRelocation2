package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
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
import net.blay09.mods.refinedrelocation.grid.SortingInventory;
import net.blay09.mods.refinedrelocation.network.GuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class ContainerRootFilter extends ContainerMod {

    public static final String KEY_ROOT_FILTER = "RootFilter";
    public static final String KEY_ADD_FILTER = "AddFilter";
    public static final String KEY_EDIT_FILTER = "EditFilter";
    public static final String KEY_DELETE_FILTER = "DeleteFilter";
    public static final String KEY_PRIORITY = "Priority";
    public static final String KEY_BLACKLIST = "Blacklist";
    public static final String KEY_BLACKLIST_INDEX = "FilterIndex";

    private final EntityPlayer entityPlayer;
    private final TileEntity tileEntity;
    private final IRootFilter rootFilter;

    private ReturnCallback returnCallback;
    private LazyOptional<ISortingInventory> sortingInventoryCap;

    private int lastFilterCount = -1;
    private int lastPriority;
    private final boolean[] lastBlacklist = new boolean[3];

    public ContainerRootFilter(EntityPlayer player, TileEntity tileEntity) {
        this(player, tileEntity, tileEntity.getCapability(CapabilityRootFilter.CAPABILITY));
    }

    public ContainerRootFilter(EntityPlayer player, TileEntity tileEntity, LazyOptional<IRootFilter> rootFilter) {
        this.entityPlayer = player;
        this.tileEntity = tileEntity;
        this.rootFilter = rootFilter.orElseGet(RootFilter::new);
        sortingInventoryCap = tileEntity.getCapability(CapabilitySortingInventory.CAPABILITY);

        addPlayerInventory(player, 128);
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
                NBTTagCompound compound = new NBTTagCompound();
                compound.putInt(KEY_BLACKLIST_INDEX, i);
                compound.putBoolean(KEY_BLACKLIST, nowBlacklist);
                RefinedRelocationAPI.syncContainerValue(KEY_BLACKLIST, compound, listeners);
                lastBlacklist[i] = nowBlacklist;
            }
        }

        sortingInventoryCap.filter(it -> it.getPriority() != lastPriority).ifPresent(sortingInventory -> {
            RefinedRelocationAPI.syncContainerValue(KEY_PRIORITY, sortingInventory.getPriority(), listeners);
            lastPriority = sortingInventory.getPriority();
        });

    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        ItemStack itemStack = super.slotClick(slotId, dragType, clickTypeIn, player);
        RefinedRelocationAPI.updateFilterPreview(player, tileEntity, rootFilter);
        return itemStack;
    }

    private void syncFilterList() {
        NBTTagCompound tagCompound = new NBTTagCompound();
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
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
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
        if (message.getKey().equals(KEY_ADD_FILTER)) {
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
            RefinedRelocation.proxy.openGui(entityPlayer, new MessageOpenGui(GuiHandler.GUI_ANY_FILTER, tileEntity.getPos(), rootFilter.getFilterCount() - 1));
        } else if (message.getKey().equals(KEY_EDIT_FILTER)) {
            int index = message.getIntValue();
            if (index < 0 || index >= rootFilter.getFilterCount()) {
                // Client tried to edit a filter that doesn't exist. Bad client!
                return;
            }
            IFilter filter = rootFilter.getFilter(index);
            if (filter != null) {
                RefinedRelocation.proxy.openGui(entityPlayer, new MessageOpenGui(GuiHandler.GUI_ANY_FILTER, tileEntity.getPos(), index));
            }
        } else if (message.getKey().equals(KEY_DELETE_FILTER)) {
            int index = message.getIntValue();
            if (index < 0 || index >= rootFilter.getFilterCount()) {
                // Client tried to delete a filter that doesn't exist. Bad client!
                return;
            }
            rootFilter.removeFilter(index);
            tileEntity.markDirty();
        } else if (message.getKey().equals(KEY_PRIORITY)) {
            int value = message.getIntValue();
            if (value < Priority.LOWEST || value > Priority.HIGHEST) {
                // Client tried to set an invalid priority. Bad client!
                return;
            }
            sortingInventoryCap.ifPresent(sortingInventory -> {
                sortingInventory.setPriority(value);
                tileEntity.markDirty();
            });
        } else if (message.getKey().equals(KEY_BLACKLIST)) {
            NBTTagCompound tagCompound = message.getNBTValue();
            int index = tagCompound.getInt(KEY_BLACKLIST_INDEX);
            if (index < 0 || index >= rootFilter.getFilterCount()) {
                // Client tried to delete a filter that doesn't exist. Bad client!
                return;
            }
            rootFilter.setIsBlacklist(index, tagCompound.getBoolean(KEY_BLACKLIST));
            tileEntity.markDirty();
            RefinedRelocationAPI.updateFilterPreview(entityPlayer, tileEntity, rootFilter);
        }
    }

    @Override
    public void receivedMessageClient(IContainerMessage message) {
        if (message.getKey().equals(KEY_ROOT_FILTER)) {
            rootFilter.deserializeNBT(message.getNBTValue().getCompound(KEY_ROOT_FILTER));
        } else if (message.getKey().equals(KEY_PRIORITY)) {
            getSortingInventory().setPriority(message.getIntValue());
        } else if (message.getKey().equals(KEY_BLACKLIST)) {
            NBTTagCompound compound = message.getNBTValue();
            rootFilter.setIsBlacklist(compound.getInt(KEY_BLACKLIST_INDEX), compound.getBoolean(KEY_BLACKLIST));
        }
    }

    public IRootFilter getRootFilter() {
        return rootFilter;
    }

    public boolean hasSortingInventory() {
        return tileEntity.getCapability(CapabilitySortingInventory.CAPABILITY).isPresent();
    }

    public ISortingInventory getSortingInventory() {
        // TODO create dummy for client
        return sortingInventoryCap;
    }

    @Nullable
    public ReturnCallback getReturnCallback() {
        return returnCallback;
    }

    public ContainerRootFilter setReturnCallback(@Nullable ReturnCallback returnCallback) {
        this.returnCallback = returnCallback;
        return this;
    }
}
