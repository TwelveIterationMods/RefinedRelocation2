package net.blay09.mods.refinedrelocation.network;

import net.minecraft.network.PacketBuffer;

public class MessageContainerString extends MessageContainer {

    private final String value;

    public MessageContainerString(String key, String value) {
        super(key);
        this.value = value;
    }

    public static void encode(MessageContainerString message, PacketBuffer buf) {
        buf.writeString(message.key);
        buf.writeString(message.value);
    }

    public static MessageContainerString decode(PacketBuffer buf) {
        String key = buf.readString(Short.MAX_VALUE);
        String value = buf.readString(Short.MAX_VALUE);
        return new MessageContainerString(key, value);
    }

    @Override
    public String getStringValue() {
        return value;
    }
}
