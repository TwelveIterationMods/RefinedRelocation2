package net.blay09.mods.refinedrelocation.network;

import net.minecraft.network.PacketBuffer;

public class MessageContainerInt extends MessageContainer {

    private final int value;

    public MessageContainerInt(String key, int value) {
        super(key);
        this.value = value;
    }

    public static void encode(MessageContainerInt message, PacketBuffer buf) {
        buf.writeString(message.key);
        buf.writeInt(message.value);
    }

    public static MessageContainerInt decode(PacketBuffer buf) {
        String key = buf.readString(Short.MAX_VALUE);
        int value = buf.readInt();
        return new MessageContainerInt(key, value);
    }

    @Override
    public int getIntValue() {
        return value;
    }
}
