package net.blay09.mods.refinedrelocation.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageContainer implements IMessage {

	public static final int TYPE_INT = 0;
	public static final int TYPE_STRING = 1;

	private int type;
	private String key;
	private int intValue;
	private String stringValue;

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

	@Override
	public void fromBytes(ByteBuf buf) {
		key = ByteBufUtils.readUTF8String(buf);
		int type = buf.readByte();
		if(type == TYPE_INT) {
			intValue = buf.readInt();
		} else if(type == TYPE_STRING) {
			stringValue = ByteBufUtils.readUTF8String(buf);
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
		}
	}

	public String getKey() {
		return key;
	}

	public int getIntValue() {
		return intValue;
	}

	public String getStringValue() {
		return stringValue;
	}

}
