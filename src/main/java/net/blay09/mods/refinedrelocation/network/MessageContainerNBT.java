package net.blay09.mods.refinedrelocation.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;

public class MessageContainerNBT extends MessageContainer {

    private final CompoundNBT value;

    public MessageContainerNBT(String key, @Nullable CompoundNBT value) {
        super(key);
        this.value = value;
    }

    public static void encode(MessageContainerNBT message, PacketBuffer buf) {
        buf.writeString(message.key);
        buf.writeCompoundTag(message.value);
    }

    public static MessageContainerNBT decode(PacketBuffer buf) {
        String key = buf.readString(Short.MAX_VALUE);
        CompoundNBT value = buf.readCompoundTag();
        return new MessageContainerNBT(key, value);
    }

    @Override
    public CompoundNBT getNBTValue() {
        return value != null ? value : new CompoundNBT();
    }
}
