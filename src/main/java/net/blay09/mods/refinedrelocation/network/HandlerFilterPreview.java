package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.client.FilterPreviewHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HandlerFilterPreview implements IMessageHandler<MessageFilterPreview, IMessage> {
	@Override
	public IMessage onMessage(final MessageFilterPreview message, MessageContext ctx) {
		RefinedRelocation.proxy.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				FilterPreviewHandler.setSlotStates(message.getSlotStates());
			}
		});
		return null;
	}
}
