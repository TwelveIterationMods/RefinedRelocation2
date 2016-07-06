package net.blay09.mods.refinedrelocation.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageLoginSyncList implements IMessage {

	public static final int TYPE_CREATIVETABS = 0;
	public static final int TYPE_MODS = 1;

	private int type;
	private String[] values;

	public MessageLoginSyncList() {
	}

	public MessageLoginSyncList(int type, String[] values) {
		this.type = type;
		this.values = values;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		type = buf.readByte();
		values = new String[buf.readShort()];
		for(int i = 0; i < values.length; i++) {
			values[i] = ByteBufUtils.readUTF8String(buf);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(type);
		buf.writeShort(values.length);
		for (String value : values) {
			ByteBufUtils.writeUTF8String(buf, value);
		}
	}

	public boolean isCreativeTabs() {
		return type == TYPE_CREATIVETABS;
	}

	public boolean isMods() {
		return type == TYPE_MODS;
	}

	public String[] getValues() {
		return values;
	}
}
