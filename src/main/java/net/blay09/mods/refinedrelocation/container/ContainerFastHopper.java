package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.tile.TileFastHopper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFastHopper extends ContainerMod {

	private final TileFastHopper tileEntity;

	public ContainerFastHopper(EntityPlayer player, TileFastHopper tileEntity) {
		this.tileEntity = tileEntity;

		for (int i = 0; i < 5; i++) {
			addSlot(new SlotItemHandlerCareless(tileEntity.getItemHandler(), i, 44 + i * 18, 20));
		}

		addPlayerInventory(player, 51);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			itemStack = slotStack.copy();

			if (index < 5) {
				if (!mergeItemStack(slotStack, 5, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(slotStack, 0, 5, false)) {
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
	public boolean canInteractWith(EntityPlayer player) {
		return !tileEntity.isRemoved() && player.getDistanceSq(tileEntity.getPos().getX() + 0.5, tileEntity.getPos().getY() + 0.5, tileEntity.getPos().getZ() + 0.5) <= 64;
	}

}
