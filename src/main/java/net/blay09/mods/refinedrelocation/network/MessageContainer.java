package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.api.container.IContainerMessage;
import net.blay09.mods.refinedrelocation.api.container.IContainerNetworked;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class MessageContainer implements IContainerMessage {

    protected final String key;

    public MessageContainer(String key) {
        this.key = key;
    }

    public static void handle(MessageContainer message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.runForDist(() -> () -> {
                Container container = Minecraft.getInstance().player.openContainer;
                if (container instanceof IContainerNetworked) {
                    ((IContainerNetworked) container).receivedMessageClient(message);
                }
                return null;
            }, () -> () -> {
                EntityPlayer player = context.getSender();
                if (player != null) {
                    Container container = player.openContainer;
                    if (container instanceof IContainerNetworked) {
                        ((IContainerNetworked) container).receivedMessageServer(message);
                    }
                }
                return null;
            });
        });
    }

    @Override
    public String getKey() {
        return key;
    }

}
