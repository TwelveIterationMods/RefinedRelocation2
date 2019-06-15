package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.api.container.IContainerMessage;
import net.blay09.mods.refinedrelocation.api.container.IContainerNetworked;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
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
            if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
                PlayerEntity player = context.getSender();
                if (player != null) {
                    Container container = player.openContainer;
                    if (container instanceof IContainerNetworked) {
                        ((IContainerNetworked) container).receivedMessageServer(message);
                    }
                }
            } else {
                DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                    Container container = Minecraft.getInstance().player.openContainer;
                    if (container instanceof IContainerNetworked) {
                        ((IContainerNetworked) container).receivedMessageClient(message);
                    }
                });
            }
        });
    }

    @Override
    public String getKey() {
        return key;
    }

}
