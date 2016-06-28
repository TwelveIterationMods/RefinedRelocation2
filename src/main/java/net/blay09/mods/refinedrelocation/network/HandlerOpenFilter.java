package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class HandlerOpenFilter implements IMessageHandler<MessageOpenFilter, IMessage> {

	@Override
	@Nullable
	public IMessage onMessage(final MessageOpenFilter message, final MessageContext ctx) {
		RefinedRelocation.proxy.addScheduledTask(new Runnable() {
			@Override
			public void run() {

			}
		});
		return null;
	}

}
