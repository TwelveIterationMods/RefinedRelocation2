package net.blay09.mods.refinedrelocation.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageRequestFilterGUI {
    private final BlockPos pos;

    public MessageRequestFilterGUI(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(MessageRequestFilterGUI message, PacketBuffer buf) {
        buf.writeBlockPos(message.pos);
    }

    public static MessageRequestFilterGUI decode(PacketBuffer buf) {
        BlockPos pos = buf.readBlockPos();
        return new MessageRequestFilterGUI(pos);
    }

    public static void handle(MessageRequestFilterGUI message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {

        });
    }
}
