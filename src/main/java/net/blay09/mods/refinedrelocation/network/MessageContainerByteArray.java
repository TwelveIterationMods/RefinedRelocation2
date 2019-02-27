package net.blay09.mods.refinedrelocation.network;

import net.minecraft.network.PacketBuffer;

public class MessageContainerByteArray extends MessageContainer {

    private final byte[] value;

    public MessageContainerByteArray(String key, byte[] value) {
        super(key);
        this.value = value;
    }

    public static void encode(MessageContainerByteArray message, PacketBuffer buf) {
        buf.writeString(message.key);
        buf.writeByteArray(message.value);
    }

    public static MessageContainerByteArray decode(PacketBuffer buf) {
        String key = buf.readString(Short.MAX_VALUE);
        byte[] value = buf.readByteArray();
        return new MessageContainerByteArray(key, value);
    }

    @Override
    public byte[] getByteArrayValue() {
        return value;
    }
}
