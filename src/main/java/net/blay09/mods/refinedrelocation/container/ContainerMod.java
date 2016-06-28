package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.network.MessageContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerMod extends Container implements IContainerNetworked {

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return false;
	}

	@Override
	public void receivedMessageClient(MessageContainer message) {

	}

	@Override
	public void receivedMessageServer(MessageContainer message) {

	}

}
