package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.container.IMessageContainer;
import net.blay09.mods.refinedrelocation.filter.NameFilter;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerNameFilter extends ContainerMod {

	public static final String KEY_VALUE = "Value";

	private final NameFilter filter;

	private String lastValue = "";

	public ContainerNameFilter(NameFilter filter) {
		this.filter = filter;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		if (!lastValue.equals(filter.getValue())) {
			RefinedRelocationAPI.syncContainerValue(KEY_VALUE, filter.getValue(), listeners);
			lastValue = filter.getValue();
		}
	}

	@Override
	public void receivedMessageClient(IMessageContainer message) {
		if(message.getKey().equals(KEY_VALUE)) {
			filter.setValue(message.getStringValue());
		}
	}

	@Override
	public void receivedMessageServer(IMessageContainer message) {
		if(message.getKey().equals(KEY_VALUE)) {
			filter.setValue(message.getStringValue());
		}
	}
}
