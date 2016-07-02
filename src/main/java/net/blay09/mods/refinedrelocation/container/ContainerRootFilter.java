package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.Priority;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.blay09.mods.refinedrelocation.api.container.IContainerMessage;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingInventory;
import net.blay09.mods.refinedrelocation.filter.FilterRegistry;
import net.blay09.mods.refinedrelocation.filter.RootFilter;
import net.blay09.mods.refinedrelocation.grid.SortingInventory;
import net.blay09.mods.refinedrelocation.network.GuiHandler;
import net.blay09.mods.refinedrelocation.network.MessageOpenGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public class ContainerRootFilter extends ContainerMod {

	public static final String KEY_FILTER_LIST = "FilterList";
	public static final String KEY_ADD_FILTER = "AddFilter";
	public static final String KEY_EDIT_FILTER = "EditFilter";
	public static final String KEY_DELETE_FILTER = "DeleteFilter";
	public static final String KEY_PRIORITY = "Priority";

	private final EntityPlayer entityPlayer;
	private final TileOrMultipart tileEntity;
	private final IRootFilter rootFilter;

	private ISortingInventory sortingInventory;

	private int lastFilterCount = -1;
	private int lastPriority;

	public ContainerRootFilter(EntityPlayer player, TileOrMultipart tileEntity) {
		this.entityPlayer = player;
		this.tileEntity = tileEntity;
		IRootFilter rootFilter = tileEntity.getCapability(CapabilityRootFilter.CAPABILITY, null);
		if(rootFilter == null) {
			rootFilter = new RootFilter();
		}
		this.rootFilter = rootFilter;
		sortingInventory = tileEntity.getCapability(CapabilitySortingInventory.CAPABILITY, null);

		addPlayerInventory(player, 128);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		if(rootFilter.getFilterCount() != lastFilterCount) {
			syncFilterList();
		}

		if(sortingInventory != null && sortingInventory.getPriority() != lastPriority) {
			RefinedRelocationAPI.syncContainerValue(KEY_PRIORITY, sortingInventory.getPriority(), listeners);
			lastPriority = sortingInventory.getPriority();
		}
	}

	private void syncFilterList() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setTag(KEY_FILTER_LIST, rootFilter.serializeNBT());
		RefinedRelocationAPI.syncContainerValue(KEY_FILTER_LIST, tagCompound, listeners);
		lastFilterCount = rootFilter.getFilterCount();
	}

	public TileOrMultipart getTileEntity() {
		return tileEntity;
	}

	@Nullable
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemStack = null;
		Slot slot = inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			itemStack = slotStack.copy();

			if (index < 27) {
				if (!mergeItemStack(slotStack, 27, inventorySlots.size(), true)) {
					return null;
				}
			} else if (!mergeItemStack(slotStack, 0, 27, false)) {
				return null;
			}

			if (slotStack.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemStack;
	}

	@Override
	public void receivedMessageServer(IContainerMessage message) {
		if(message.getKey().equals(KEY_ADD_FILTER)) {
			String typeId = message.getStringValue();
			IFilter filter = FilterRegistry.createFilter(typeId);
			if(filter == null) {
				// Client tried to create a filter that doesn't exist. Bad client!
				return;
			}
			if(rootFilter.getFilterCount() >= 3) {
				// Client tried to create more than three filters. Bad client!
				return;
			}
			rootFilter.addFilter(filter);
			tileEntity.markDirty();
			lastFilterCount = rootFilter.getFilterCount();
			syncFilterList();
			RefinedRelocation.proxy.openGui(entityPlayer, new MessageOpenGui(GuiHandler.GUI_ANY_FILTER, tileEntity.getPos(), rootFilter.getFilterCount() - 1));
		} else if(message.getKey().equals(KEY_EDIT_FILTER)) {
			int index = message.getIntValue();
			if(index < 0 || index >= rootFilter.getFilterCount()) {
				// Client tried to edit a filter that doesn't exist. Bad client!
				return;
			}
			IFilter filter = rootFilter.getFilter(index);
			if(filter != null) {
				RefinedRelocation.proxy.openGui(entityPlayer, new MessageOpenGui(GuiHandler.GUI_ANY_FILTER, tileEntity.getPos(), index));
			}
		} else if(message.getKey().equals(KEY_DELETE_FILTER)) {
			int index = message.getIntValue();
			if(index < 0 || index >= rootFilter.getFilterCount()) {
				// Client tried to delete a filter that doesn't exist. Bad client!
				return;
			}
			rootFilter.removeFilter(index);
			tileEntity.markDirty();
		} else if(message.getKey().equals(KEY_PRIORITY)) {
			int value = message.getIntValue();
			if(value < Priority.LOWEST || value > Priority.HIGHEST) {
				// Client tried to set an invalid priority. Bad client!
				return;
			}
			if(sortingInventory == null) {
				// Client tried to set priority on an invalid tile. Bad client!
				return;
			}
			sortingInventory.setPriority(value);
			tileEntity.markDirty();
		}
	}

	@Override
	public void receivedMessageClient(IContainerMessage message) {
		if(message.getKey().equals(KEY_FILTER_LIST)) {
			rootFilter.deserializeNBT(message.getNBTValue().getTag(KEY_FILTER_LIST));
		} else if(message.getKey().equals(KEY_PRIORITY)) {
			if(sortingInventory == null) {
				// Create a dummy sorting inventory for the client to store its settings.
				sortingInventory = new SortingInventory();
			}
			sortingInventory.setPriority(message.getIntValue());
		}
	}

	public IRootFilter getRootFilter() {
		return rootFilter;
	}

}
