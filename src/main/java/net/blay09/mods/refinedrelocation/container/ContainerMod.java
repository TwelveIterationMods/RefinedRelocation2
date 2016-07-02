package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.api.container.IContainerNetworked;
import net.blay09.mods.refinedrelocation.api.container.IContainerMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerMod extends Container implements IContainerNetworked {

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public void receivedMessageClient(IContainerMessage message) {

	}

	@Override
	public void receivedMessageServer(IContainerMessage message) {

	}

	protected void addPlayerInventory(EntityPlayer player, int offsetY) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, offsetY + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, offsetY + 58));
		}
	}

}
