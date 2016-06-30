package net.blay09.mods.refinedrelocation.network;

import io.netty.buffer.ByteBuf;
import net.blay09.mods.refinedrelocation.api.container.IMessageContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageContainer implements IMessage, IMessageContainer {

	public static final int TYPE_INT = 0;
	public static final int TYPE_STRING = 1;
	public static final int TYPE_NBT = 2;

	private int type;
	private String key;
	private int intValue;
	private String stringValue;
	private NBTTagCompound nbtValue;

	public MessageContainer() {
	}

	public MessageContainer(String key, int value) {
		this.key = key;
		this.type = TYPE_INT;
		this.intValue = value;
	}

	public MessageContainer(String key, String value) {
		this.key = key;
		this.type = TYPE_STRING;
		this.stringValue = value;
	}

	public MessageContainer(String key, NBTTagCompound value) {
		this.key = key;
		this.type = TYPE_NBT;
		this.nbtValue = value;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		key = ByteBufUtils.readUTF8String(buf);
		int type = buf.readByte();
		if(type == TYPE_INT) {
			intValue = buf.readInt();
		} else if(type == TYPE_STRING) {
			stringValue = ByteBufUtils.readUTF8String(buf);
		} else if(type == TYPE_NBT) {
			nbtValue = ByteBufUtils.readTag(buf);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, key);
		buf.writeByte(type);
		if(type == TYPE_INT) {
			buf.writeInt(intValue);
		} else if(type == TYPE_STRING) {
			ByteBufUtils.writeUTF8String(buf, stringValue);
		} else if(type == TYPE_NBT) {
			ByteBufUtils.writeTag(buf, nbtValue);
		}
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public int getIntValue() {
		return intValue;
	}

	@Override
	public String getStringValue() {
		return stringValue;
	}

	@Override
	public NBTTagCompound getNBTValue() {
		return nbtValue;
	}
}
