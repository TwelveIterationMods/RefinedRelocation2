package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.container.IContainerMessage;
import net.blay09.mods.refinedrelocation.api.container.IContainerReturnable;
import net.blay09.mods.refinedrelocation.api.container.ReturnCallback;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public class ChecklistFilterContainer extends FilterContainer implements IContainerReturnable {

	public static final String KEY_CHECK = "Check";
	public static final String KEY_UNCHECK = "Uncheck";
	public static final String KEY_STATES = "States";

	private static final int UPDATE_INTERVAL = 20;

	private final PlayerEntity player;
	private final TileEntity tileEntity;
	private final int rootFilterIndex;
	private final IChecklistFilter filter;

	private final byte[] lastStates;
	private int ticksSinceUpdate = UPDATE_INTERVAL;
	private ReturnCallback returnCallback;

	public ChecklistFilterContainer(int windowId, PlayerInventory playerInventory, TileEntity tileEntity, int rootFilterIndex, IChecklistFilter filter) {
		super(ModContainers.checklistFilter, windowId);

		this.player = playerInventory.player;
		this.tileEntity = tileEntity;
		this.rootFilterIndex = rootFilterIndex;
		this.filter = filter;
		lastStates = new byte[filter.getOptionCount()];

		addPlayerInventory(playerInventory, 8, 128);
	}

	public IChecklistFilter getFilter() {
		return filter;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		ticksSinceUpdate++;
		if(ticksSinceUpdate >= UPDATE_INTERVAL) {
			boolean anyChanges = false;
			for (int i = 0; i < lastStates.length; i++) {
				byte isChecked = (byte) (filter.isOptionChecked(i) ? 1 : 0);
				if (lastStates[i] != isChecked) {
					anyChanges = true;
					lastStates[i] = isChecked;
				}
			}
			if (anyChanges) {
				RefinedRelocationAPI.syncContainerValue(KEY_STATES, lastStates, listeners);
				RefinedRelocationAPI.updateFilterPreview(player, tileEntity, filter);
			}
			ticksSinceUpdate = 0;
		}
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
		ItemStack itemStack = super.slotClick(slotId, dragType, clickTypeIn, player);
		RefinedRelocationAPI.updateFilterPreview(player, tileEntity, filter);
		return itemStack;
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

	public TileEntity getTileEntity() {
		return tileEntity;
	}

	@Override
	public void receivedMessageServer(IContainerMessage message) {
		if(message.getKey().equals(KEY_CHECK)) {
			int index = message.getIntValue();
			if(index < 0 || index >= lastStates.length) {
				// Client tried to check an option that doesn't exist. Bad client!
				return;
			}
			filter.setOptionChecked(index, true);
			tileEntity.markDirty();
			lastStates[index] = 1;
			RefinedRelocationAPI.updateFilterPreview(player, tileEntity, filter);
		} else if(message.getKey().equals(KEY_UNCHECK)) {
			int index = message.getIntValue();
			if(index < 0 || index >= lastStates.length) {
				// Client tried to check an option that doesn't exist. Bad client!
				return;
			}
			filter.setOptionChecked(index, false);
			tileEntity.markDirty();
			lastStates[index] = 0;
			RefinedRelocationAPI.updateFilterPreview(player, tileEntity, filter);
		}
	}

	@Override
	public void receivedMessageClient(IContainerMessage message) {
		if(message.getKey().equals(KEY_STATES)) {
			byte[] states = message.getByteArrayValue();
			for(int i = 0; i < states.length; i++) {
				filter.setOptionChecked(i, states[i] == 1);
			}
		}
	}

	@Override
	public void setReturnCallback(@Nullable ReturnCallback callback) {
		this.returnCallback = callback;
	}

	@Nullable
	@Override
	public ReturnCallback getReturnCallback() {
		return returnCallback;
	}

	@Override
	public int getRootFilterIndex() {
		return rootFilterIndex;
	}
}
