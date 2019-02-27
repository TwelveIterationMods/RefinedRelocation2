package net.blay09.mods.refinedrelocation.network;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;

public class MessageContainerNBT extends MessageContainer {

    private final NBTTagCompound value;

    public MessageContainerNBT(String key, @Nullable NBTTagCompound value) {
        super(key);
        this.value = value;
    }

    public static void encode(MessageContainerNBT message, PacketBuffer buf) {
        buf.writeString(message.key);
        buf.writeCompoundTag(message.value);
    }

    public static MessageContainerNBT decode(PacketBuffer buf) {
        String key = buf.readString(Short.MAX_VALUE);
        NBTTagCompound value = buf.readCompoundTag();
        return new MessageContainerNBT(key, value);
    }

    @Override
    public NBTTagCompound getNBTValue() {
        return value != null ? value : new NBTTagCompound();
    }
}
