package net.blay09.mods.refinedrelocation.network;

import io.netty.buffer.ByteBuf;
import net.blay09.mods.refinedrelocation.api.container.IContainerMessage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageContainer implements IMessage, IContainerMessage {

	public static final int TYPE_INT = 0;
	public static final int TYPE_STRING = 1;
	public static final int TYPE_NBT = 2;
	public static final int TYPE_BYTE_ARRAY = 3;
	public static final int TYPE_INT_TWO = 4;

	private int type;
	private String key;
	private int intValue;
	private int secondaryIntValue;
	private String stringValue;
	private NBTTagCompound nbtValue;
	private byte[] byteArrayValue;

	public MessageContainer() {
	}

	public MessageContainer(String key, int value) {
		this.key = key;
		this.type = TYPE_INT;
		this.intValue = value;
	}

	public MessageContainer(String key, int value, int value2) {
		this.key = key;
		this.type = TYPE_INT_TWO;
		this.intValue = value;
		this.secondaryIntValue = value2;
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

	public MessageContainer(String key, byte[] value) {
		this.key = key;
		this.type = TYPE_BYTE_ARRAY;
		this.byteArrayValue = value;
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
		} else if(type == TYPE_BYTE_ARRAY) {
			byteArrayValue = new byte[buf.readShort()];
			buf.readBytes(byteArrayValue);
		} else if(type == TYPE_INT_TWO) {
			intValue = buf.readInt();
			secondaryIntValue = buf.readInt();
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
		} else if(type == TYPE_BYTE_ARRAY) {
			buf.writeShort(byteArrayValue.length);
			buf.writeBytes(byteArrayValue);
		} else if(type == TYPE_INT_TWO) {
			buf.writeInt(intValue);
			buf.writeInt(secondaryIntValue);
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
	public int getSecondaryIntValue() {
		return secondaryIntValue;
	}

	@Override
	public String getStringValue() {
		return stringValue;
	}

	@Override
	public NBTTagCompound getNBTValue() {
		return nbtValue;
	}

	@Override
	public byte[] getByteArrayValue() {
		return byteArrayValue;
	}
}
