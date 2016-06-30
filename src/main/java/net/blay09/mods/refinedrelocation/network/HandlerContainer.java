package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.container.IContainerNetworked;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

public class HandlerContainer implements IMessageHandler<MessageContainer, IMessage> {

	@Override
	@Nullable
	public IMessage onMessage(final MessageContainer message, final MessageContext ctx) {
		RefinedRelocation.proxy.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				if(ctx.side == Side.CLIENT) {
					Container container = FMLClientHandler.instance().getClientPlayerEntity().openContainer;
					if (container instanceof IContainerNetworked) {
						((IContainerNetworked) container).receivedMessageClient(message);
					}
				} else {
					Container container = ctx.getServerHandler().playerEntity.openContainer;
					if (container instanceof IContainerNetworked) {
						((IContainerNetworked) container).receivedMessageServer(message);
					}
				}
			}
		});
		return null;
	}

}
