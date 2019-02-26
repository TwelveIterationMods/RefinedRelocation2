package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.client.FilterPreviewHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageFilterPreview {

    public static final int STATE_FAILURE = 0;
    public static final int STATE_SUCCESS = 1;

    public static final int INVENTORY_SLOT_COUNT = 36;

    private final byte[] slotStates;

    public MessageFilterPreview(byte[] slotStates) {
        this.slotStates = slotStates;
        assert slotStates.length == INVENTORY_SLOT_COUNT;
    }

    public static void encode(MessageFilterPreview message, PacketBuffer buf) {
        buf.writeBytes(message.slotStates);
    }

    public static MessageFilterPreview decode(PacketBuffer buf) {
        byte[] slotStates = new byte[INVENTORY_SLOT_COUNT];
        buf.readBytes(slotStates);
        return new MessageFilterPreview(slotStates);
    }

    public static void handle(MessageFilterPreview message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            FilterPreviewHandler.setSlotStates(message.slotStates);
        });
    }
}
