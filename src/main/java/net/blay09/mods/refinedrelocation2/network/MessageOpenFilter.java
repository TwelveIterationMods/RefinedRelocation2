package net.blay09.mods.refinedrelocation2.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageOpenFilter implements IMessage {

    private BlockPos pos;

    public MessageOpenFilter() {
    }

    public MessageOpenFilter(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
    }

    public BlockPos getPos() {
        return pos;
    }
}
