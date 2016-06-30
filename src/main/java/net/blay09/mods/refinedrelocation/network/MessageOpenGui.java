package net.blay09.mods.refinedrelocation.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageOpenGui implements IMessage {

	private static final int TYPE_BLOCK = 0;
	private static final int TYPE_INVENTORY = 1;

	private int windowId;
	private int id;
	private int type;

	private int slotIndex;

	private BlockPos pos;

	public MessageOpenGui() {
	}

	public MessageOpenGui(int id, BlockPos pos) {
		this.id = id;
		this.type = TYPE_BLOCK;
		this.pos = pos;
	}

	public MessageOpenGui(int id, int slotIndex) {
		this.id = id;
		this.type = TYPE_INVENTORY;
		this.slotIndex = slotIndex;
	}

	public MessageOpenGui setWindowId(int windowId) {
		this.windowId = windowId;
		return this;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		id = buf.readByte();
		windowId = buf.readInt();
		type = buf.readByte();
		if(type == TYPE_BLOCK) {
			pos = BlockPos.fromLong(buf.readLong());
		} else if(type == TYPE_INVENTORY) {
			slotIndex = buf.readShort();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(id);
		buf.writeInt(windowId);
		buf.writeByte(type);
		if(type == 0) {
			buf.writeLong(pos.toLong());
		} else if(type == 1) {
			buf.writeShort(slotIndex);
		}
	}

	public int getId() {
		return id;
	}

	public int getWindowId() {
		return windowId;
	}

	public boolean isBlock() {
		return type == TYPE_BLOCK;
	}

	public boolean isInventory() {
		return type == TYPE_INVENTORY;
	}

	public int getSlotIndex() {
		return slotIndex;
	}

	public BlockPos getPos() {
		return pos;
	}
}
