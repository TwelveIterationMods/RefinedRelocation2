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
import net.blay09.mods.refinedrelocation.network.MessageFilterPreview;
import net.blay09.mods.refinedrelocation.network.MessageOpenGui;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class ContainerRootFilter extends ContainerMod {

	public static final String KEY_FILTER_LIST = "FilterList";
	public static final String KEY_ADD_FILTER = "AddFilter";
	public static final String KEY_EDIT_FILTER = "EditFilter";
	public static final String KEY_DELETE_FILTER = "DeleteFilter";
	public static final String KEY_PRIORITY = "Priority";
	public static final String KEY_BLACKLIST = "Blacklist";
	public static final String KEY_BLACKLIST_INDEX = "FilterIndex";

	private final EntityPlayer entityPlayer;
	private final TileOrMultipart tileEntity;
	private final IRootFilter rootFilter;

	private ISortingInventory sortingInventory;

	private int lastFilterCount = -1;
	private int lastPriority;
	private boolean[] lastBlacklist = new boolean[3];

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
			RefinedRelocationAPI.updateFilterPreview(entityPlayer, tileEntity, rootFilter);
		}

		for(int i = 0; i < lastBlacklist.length; i++) {
			boolean nowBlacklist = rootFilter.isBlacklist(i);
			if(lastBlacklist[i] != nowBlacklist) {
				NBTTagCompound compound = new NBTTagCompound();
				compound.setInteger(KEY_BLACKLIST_INDEX, i);
				compound.setBoolean(KEY_BLACKLIST, nowBlacklist);
				RefinedRelocationAPI.syncContainerValue(KEY_BLACKLIST, compound, listeners);
				lastBlacklist[i] = nowBlacklist;
			}
		}

		if(sortingInventory != null && sortingInventory.getPriority() != lastPriority) {
			RefinedRelocationAPI.syncContainerValue(KEY_PRIORITY, sortingInventory.getPriority(), listeners);
			lastPriority = sortingInventory.getPriority();
		}
	}

	@Nullable
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		ItemStack itemStack = super.slotClick(slotId, dragType, clickTypeIn, player);
		RefinedRelocationAPI.updateFilterPreview(player, tileEntity, rootFilter);
		return itemStack;
	}

	private void syncFilterList() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setTag(KEY_FILTER_LIST, rootFilter.serializeNBT());
		RefinedRelocationAPI.syncContainerValue(KEY_FILTER_LIST, tagCompound, listeners);
		lastFilterCount = rootFilter.getFilterCount();
		for(int i = 0; i < lastBlacklist.length; i++) {
			lastBlacklist[i] = rootFilter.isBlacklist(i);
		}
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
			RefinedRelocationAPI.updateFilterPreview(entityPlayer, tileEntity, rootFilter);
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
		} else if(message.getKey().equals(KEY_BLACKLIST)) {
			NBTTagCompound tagCompound = message.getNBTValue();
			int index = tagCompound.getInteger(KEY_BLACKLIST_INDEX);
			if(index < 0 || index >= rootFilter.getFilterCount()) {
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
		if(message.getKey().equals(KEY_FILTER_LIST)) {
			rootFilter.deserializeNBT(message.getNBTValue().getTagList(KEY_FILTER_LIST, Constants.NBT.TAG_COMPOUND));
		} else if(message.getKey().equals(KEY_PRIORITY)) {
			getSortingInventory().setPriority(message.getIntValue());
		} else if(message.getKey().equals(KEY_BLACKLIST)) {
			NBTTagCompound compound = message.getNBTValue();
			rootFilter.setIsBlacklist(compound.getInteger(KEY_BLACKLIST_INDEX), compound.getBoolean(KEY_BLACKLIST));
		}
	}

	public IRootFilter getRootFilter() {
		return rootFilter;
	}

	public ISortingInventory getSortingInventory() {
		if(sortingInventory == null) {
			// Create a dummy sorting inventory for the client to store its settings.
			sortingInventory = new SortingInventory();
		}
		return sortingInventory;
	}
}
