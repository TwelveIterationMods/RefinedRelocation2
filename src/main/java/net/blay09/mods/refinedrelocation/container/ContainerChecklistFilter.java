package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.blay09.mods.refinedrelocation.api.container.IContainerMessage;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerChecklistFilter extends ContainerMod {

	public static final String KEY_CHECK = "Check";
	public static final String KEY_UNCHECK = "Uncheck";
	public static final String KEY_STATES = "States";

	private static final int UPDATE_INTERVAL = 20;

	private final EntityPlayer player;
	private final TileOrMultipart tileEntity;
	private final IChecklistFilter filter;

	private final byte[] lastStates;
	private int ticksSinceUpdate = UPDATE_INTERVAL;

	public ContainerChecklistFilter(EntityPlayer player, TileOrMultipart tileEntity, IChecklistFilter filter) {
		this.player = player;
		this.tileEntity = tileEntity;
		this.filter = filter;
		lastStates = new byte[filter.getOptionCount()];

		addPlayerInventory(player, 128);
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
			}
			ticksSinceUpdate = 0;
		}
	}

	public TileOrMultipart getTileEntity() {
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
			lastStates[index] = 1;
		} else if(message.getKey().equals(KEY_UNCHECK)) {
			int index = message.getIntValue();
			if(index < 0 || index >= lastStates.length) {
				// Client tried to check an option that doesn't exist. Bad client!
				return;
			}
			filter.setOptionChecked(index, false);
			lastStates[index] = 0;
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

}
