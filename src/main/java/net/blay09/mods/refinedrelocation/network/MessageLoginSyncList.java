package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.filter.CreativeTabFilter;
import net.blay09.mods.refinedrelocation.filter.ModFilter;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageLoginSyncList {

    public static final int TYPE_CREATIVE_TABS = 0;
    public static final int TYPE_MODS = 1;

    private final int type;
    private final String[] values;

    public MessageLoginSyncList(int type, String[] values) {
        this.type = type;
        this.values = values;
    }

    public static void encode(MessageLoginSyncList message, PacketBuffer buf) {
        buf.writeByte(message.type);
        buf.writeShort(message.values.length);
        for (String value : message.values) {
            buf.writeString(value);
        }
    }

    public static MessageLoginSyncList decode(PacketBuffer buf) {
        int type = buf.readByte();
        String[] values = new String[buf.readShort()];
        for (int i = 0; i < values.length; i++) {
            values[i] = buf.readString(Short.MAX_VALUE);
        }
        return new MessageLoginSyncList(type, values);
    }

    public static void handle(MessageLoginSyncList message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (message.type == TYPE_CREATIVE_TABS) {
                CreativeTabFilter.creativeTabs = message.values;
            } else if (message.type == TYPE_MODS) {
                ModFilter.setModList(message.values);
            }
        });
    }
}
