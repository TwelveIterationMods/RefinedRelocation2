package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.container.IMessageContainer;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.blay09.mods.refinedrelocation.filter.NameFilter;
import net.blay09.mods.refinedrelocation.filter.RootFilter;
import net.blay09.mods.refinedrelocation.util.TileWrapper;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerNameFilter extends ContainerMod {

	public static final String KEY_VALUE = "Value";

	private final EntityPlayer player;
	private final TileWrapper tileWrapper;
	private final IRootFilter rootFilter;
	private final int filterIndex;
	private final NameFilter filter;

	private String lastValue = "";

	private boolean guiNeedsUpdate;

	public ContainerNameFilter(EntityPlayer player, TileWrapper tileWrapper, int filterIndex) {
		this.player = player;
		this.tileWrapper = tileWrapper;
		this.filterIndex = filterIndex;
		IRootFilter rootFilter = tileWrapper.getCapability(CapabilityRootFilter.CAPABILITY, null);
		if(rootFilter == null) {
			rootFilter = new RootFilter();
		}
		this.rootFilter = rootFilter;
		IFilter filter = rootFilter.getFilter(filterIndex);
		if(filter instanceof NameFilter) {
			this.filter = (NameFilter) filter;
		} else {
			this.filter = new NameFilter();
		}
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
			markGuiNeedsUpdate(true);
		}
	}

	@Override
	public void receivedMessageServer(IMessageContainer message) {
		if(message.getKey().equals(KEY_VALUE)) {
			filter.setValue(message.getStringValue());
			lastValue = filter.getValue();
		}
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
}
