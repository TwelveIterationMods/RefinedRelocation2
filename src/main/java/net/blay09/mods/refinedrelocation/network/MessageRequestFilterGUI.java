package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageRequestFilterGUI {
    private final BlockPos pos;
    private final int rootFilterIndex;

    public MessageRequestFilterGUI(BlockPos pos, int rootFilterIndex) {
        this.pos = pos;
        this.rootFilterIndex = rootFilterIndex;
    }

    public static void encode(MessageRequestFilterGUI message, PacketBuffer buf) {
        buf.writeBlockPos(message.pos);
        buf.writeByte(message.rootFilterIndex);
    }

    public static MessageRequestFilterGUI decode(PacketBuffer buf) {
        BlockPos pos = buf.readBlockPos();
        int rootFilterIndex = buf.readByte();
        return new MessageRequestFilterGUI(pos, rootFilterIndex);
    }

    public static void handle(MessageRequestFilterGUI message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            PlayerEntity player = context.getSender();
            if (player == null) {
                return;
            }

            TileEntity tileEntity = player.world.getTileEntity(message.pos);
            if (tileEntity != null) {
                RefinedRelocationAPI.openRootFilterGui(player, tileEntity, message.rootFilterIndex);
            }
        });
        context.setPacketHandled(true);
    }
}
