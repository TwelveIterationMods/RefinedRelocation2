package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.filter.CreativeTabFilter;
import net.blay09.mods.refinedrelocation.filter.ModFilter;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class HandlerLoginSyncList implements IMessageHandler<MessageLoginSyncList, IMessage> {

	@Override
	@Nullable
	public IMessage onMessage(final MessageLoginSyncList message, MessageContext ctx) {
		NetworkHandler.getThreadListener(ctx).addScheduledTask(new Runnable() {
			@Override
			public void run() {
				if(message.isCreativeTabs()) {
					CreativeTabFilter.creativeTabs = message.getValues();
				} else if(message.isMods()) {
					ModFilter.setModList(message.getValues());
				}
			}
		});
		return null;
	}

}
