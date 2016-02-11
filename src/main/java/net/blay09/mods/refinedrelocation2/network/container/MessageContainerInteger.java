package net.blay09.mods.refinedrelocation2.network.container;

import io.netty.buffer.ByteBuf;

public class MessageContainerInteger extends MessageContainer {

    private int value;

    public MessageContainerInteger() {
    }

    public MessageContainerInteger(String name, int value) {
        super(name);
        this.value = value;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        value = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(value);
    }

    public int getValue() {
        return value;
    }
}
