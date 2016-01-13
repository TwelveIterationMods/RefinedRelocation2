package net.blay09.mods.refinedrelocation2.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageScrollIndex implements IMessage {

    private int index;

    public MessageScrollIndex() {
    }

    public MessageScrollIndex(int index) {
        this.index = index;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        index = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(index);
    }

    public int getIndex() {
        return index;
    }

}
