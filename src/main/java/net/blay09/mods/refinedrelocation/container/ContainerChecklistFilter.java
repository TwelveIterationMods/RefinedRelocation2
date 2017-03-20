package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.container.IContainerMessage;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerChecklistFilter extends ContainerMod {

	public static final String KEY_CHECK = "Check";
	public static final String KEY_UNCHECK = "Uncheck";
	public static final String KEY_STATES = "States";

	private static final int UPDATE_INTERVAL = 20;

	private final EntityPlayer player;
	private final TileEntity tileEntity;
	private final IChecklistFilter filter;

	private final byte[] lastStates;
	private int ticksSinceUpdate = UPDATE_INTERVAL;

	public ContainerChecklistFilter(EntityPlayer player, TileEntity tileEntity, IChecklistFilter filter) {
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
				RefinedRelocationAPI.updateFilterPreview(player, tileEntity, filter);
			}
			ticksSinceUpdate = 0;
		}
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		ItemStack itemStack = super.slotClick(slotId, dragType, clickTypeIn, player);
		RefinedRelocationAPI.updateFilterPreview(player, tileEntity, filter);
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

}
