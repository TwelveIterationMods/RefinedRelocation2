package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.blay09.mods.refinedrelocation.api.container.IMessageContainer;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.blay09.mods.refinedrelocation.filter.FilterRegistry;
import net.blay09.mods.refinedrelocation.filter.RootFilter;
import net.blay09.mods.refinedrelocation.network.GuiHandler;
import net.blay09.mods.refinedrelocation.network.MessageOpenGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public class ContainerRootFilter extends ContainerMod {

	public static final String KEY_FILTER = "Filter";
	public static final String KEY_ADD_FILTER = "AddFilter";
	public static final String KEY_EDIT_FILTER = "EditFilter";
	public static final String KEY_DELETE_FILTER = "DeleteFilter";

	private final EntityPlayer entityPlayer;
	private final TileOrMultipart tileEntity;
	private final IRootFilter rootFilter;

	private int lastFilterCount = -1;

	public ContainerRootFilter(EntityPlayer player, TileOrMultipart tileEntity) {
		this.entityPlayer = player;
		this.tileEntity = tileEntity;
		IRootFilter rootFilter = tileEntity.getCapability(CapabilityRootFilter.CAPABILITY, null);
		if(rootFilter == null) {
			rootFilter = new RootFilter();
		}
		this.rootFilter = rootFilter;

		addPlayerInventory(player, 128);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		if(rootFilter.getFilterCount() != lastFilterCount) {
			syncFilterList();
		}
	}

	private void syncFilterList() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setTag("FilterList", rootFilter.serializeNBT());
		RefinedRelocationAPI.syncContainerValue(KEY_FILTER, tagCompound, listeners);
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
	public void receivedMessageServer(IMessageContainer message) {
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
		}
	}

	@Override
	public void receivedMessageClient(IMessageContainer message) {
		if(message.getKey().equals(KEY_FILTER)) {
			rootFilter.deserializeNBT(message.getNBTValue().getTag("FilterList"));
		}
	}

	public IRootFilter getRootFilter() {
		return rootFilter;
	}

}
