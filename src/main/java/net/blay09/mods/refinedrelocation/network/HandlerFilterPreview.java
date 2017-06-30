package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.client.FilterPreviewHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class HandlerFilterPreview implements IMessageHandler<MessageFilterPreview, IMessage> {
	@Override
	@Nullable
	public IMessage onMessage(final MessageFilterPreview message, MessageContext ctx) {
		NetworkHandler.getThreadListener(ctx).addScheduledTask(() -> FilterPreviewHandler.setSlotStates(message.getSlotStates()));
		return null;
	}
}
