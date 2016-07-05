package net.blay09.mods.refinedrelocation.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageFilterPreview implements IMessage {

	public static final int STATE_FAILURE = 0;
	public static final int STATE_SUCCESS = 1;

	public static final int INVENTORY_SLOT_COUNT = 36;

	private byte[] slotStates;

	public MessageFilterPreview() {
	}

	public MessageFilterPreview(byte[] slotStates) {
		this.slotStates = slotStates;
		assert slotStates.length == INVENTORY_SLOT_COUNT;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		slotStates = new byte[INVENTORY_SLOT_COUNT];
		buf.readBytes(slotStates);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBytes(slotStates);
	}

	public byte[] getSlotStates() {
		return slotStates;
	}
}
