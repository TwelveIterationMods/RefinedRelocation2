package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.container.IContainerMessage;
import net.blay09.mods.refinedrelocation.api.container.IContainerReturnable;
import net.blay09.mods.refinedrelocation.api.container.ReturnCallback;
import net.blay09.mods.refinedrelocation.filter.NameFilter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public class ContainerNameFilter extends ContainerMod implements IContainerReturnable {

	public static final String KEY_VALUE = "Value";

	private final PlayerEntity player;
	private final TileEntity tileEntity;
	private final NameFilter filter;

	private String lastValue = "";

	private boolean guiNeedsUpdate;
	private ReturnCallback returnCallback;

	public ContainerNameFilter(int windowId, PlayerEntity player, TileEntity tileEntity, NameFilter filter) {
		super(ModContainers.nameFilter, windowId);
		this.player = player;
		this.tileEntity = tileEntity;
		this.filter = filter;

		addPlayerInventory(player, 128);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		if (!lastValue.equals(filter.getValue())) {
			RefinedRelocationAPI.syncContainerValue(KEY_VALUE, filter.getValue(), listeners);
			RefinedRelocationAPI.updateFilterPreview(player, tileEntity, filter);
			lastValue = filter.getValue();
		}
	}

	@Override
	public void receivedMessageClient(IContainerMessage message) {
		if (message.getKey().equals(KEY_VALUE)) {
			filter.setValue(message.getStringValue());
			markGuiNeedsUpdate(true);
		}
	}

	@Override
	public void receivedMessageServer(IContainerMessage message) {
		if (message.getKey().equals(KEY_VALUE)) {
			filter.setValue(message.getStringValue());
			tileEntity.markDirty();
			lastValue = filter.getValue();
			RefinedRelocationAPI.updateFilterPreview(player, tileEntity, filter);
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

	public void sendValueToServer(String value) {
		RefinedRelocationAPI.sendContainerMessageToServer(KEY_VALUE, value);
	}

	public String getValue() {
		return filter.getValue();
	}

	public void markGuiNeedsUpdate(boolean dirty) {
		this.guiNeedsUpdate = dirty;
	}

	public boolean doesGuiNeedUpdate() {
		return guiNeedsUpdate;
	}

	public TileEntity getTileEntity() {
		return tileEntity;
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
}
