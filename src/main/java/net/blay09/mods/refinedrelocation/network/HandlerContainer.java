package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.api.container.IContainerNetworked;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

public class HandlerContainer implements IMessageHandler<MessageContainer, IMessage> {

	@Override
	@Nullable
	public IMessage onMessage(final MessageContainer message, final MessageContext ctx) {
		NetworkHandler.getThreadListener(ctx).addScheduledTask(new Runnable() {
			@Override
			public void run() {
				if(ctx.side == Side.CLIENT) {
					Container container = NetworkHandler.getClientPlayerEntity().openContainer;
					if (container instanceof IContainerNetworked) {
						((IContainerNetworked) container).receivedMessageClient(message);
					} else {
						System.err.println("Got container message but open container is not networked: " + container);
					}
				} else {
					Container container = ctx.getServerHandler().player.openContainer;
					if (container instanceof IContainerNetworked) {
						((IContainerNetworked) container).receivedMessageServer(message);
					} else {
						System.err.println("Got container message but open container is not networked: " + container);
					}
				}
			}
		});
		return null;
	}

}
