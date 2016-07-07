package net.blay09.mods.refinedrelocation.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageOpenGui implements IMessage {

	private static final int TYPE_POS = 0;
	private static final int TYPE_INT = 1;
	private static final int TYPE_POS_INT = 2;

	private int windowId;
	private int id;
	private int type;

	private int intValue;

	private BlockPos pos;

	public MessageOpenGui() {
	}

	public MessageOpenGui(int id, BlockPos pos) {
		this.id = id;
		this.type = TYPE_POS;
		this.pos = pos;
	}

	public MessageOpenGui(int id, TileEntity tileEntity) {
		this.id = id;
		this.type = TYPE_POS;
		this.pos = tileEntity.getPos();
	}

//	public MessageOpenGui(int id, Multipart part) {  // @McMultipart
//		 TODO Support Multiparts in MOG
//		throw new NotImplementedException("MOG doesn't support parts yet");
//	}

	public MessageOpenGui(int id, int intValue) {
		this.id = id;
		this.type = TYPE_INT;
		this.intValue = intValue;
	}

	public MessageOpenGui(int id, BlockPos pos, int intValue) {
		this.id = id;
		this.type = TYPE_POS_INT;
		this.pos = pos;
		this.intValue = intValue;
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
		if(type == TYPE_POS) {
			pos = BlockPos.fromLong(buf.readLong());
		} else if(type == TYPE_INT) {
			intValue = buf.readInt();
		} else if(type == TYPE_POS_INT) {
			pos = BlockPos.fromLong(buf.readLong());
			intValue = buf.readInt();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(id);
		buf.writeInt(windowId);
		buf.writeByte(type);
		if(type == TYPE_POS) {
			buf.writeLong(pos.toLong());
		} else if(type == TYPE_INT) {
			buf.writeInt(intValue);
		} else if(type == TYPE_POS_INT) {
			buf.writeLong(pos.toLong());
			buf.writeInt(intValue);
		}
	}

	public int getId() {
		return id;
	}

	public int getWindowId() {
		return windowId;
	}

	public boolean hasPosition() {
		return type == TYPE_POS || type == TYPE_POS_INT;
	}

	public boolean isInt() {
		return type == TYPE_INT || type == TYPE_POS_INT;
	}

	public int getIntValue() {
		return intValue;
	}

	public BlockPos getPos() {
		return pos;
	}

}
