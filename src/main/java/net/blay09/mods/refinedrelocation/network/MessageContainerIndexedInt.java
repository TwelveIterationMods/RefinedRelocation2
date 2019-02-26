package net.blay09.mods.refinedrelocation.network;

import net.minecraft.network.PacketBuffer;

public class MessageContainerIndexedInt extends MessageContainer {

    private final int index;
    private final int value;

    public MessageContainerIndexedInt(String key, int index, int value) {
        super(key);
        this.index = index;
        this.value = value;
    }

    public static void encode(MessageContainerIndexedInt message, PacketBuffer buf) {
        buf.writeString(message.key);
        buf.writeInt(message.index);
        buf.writeInt(message.value);
    }

    public static MessageContainerIndexedInt decode(PacketBuffer buf) {
        String key = buf.readString(Short.MAX_VALUE);
        int index = buf.readInt();
        int value = buf.readInt();
        return new MessageContainerIndexedInt(key, index, value);
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public int getIntValue() {
        return value;
    }
}
