package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.filter.CreativeTabFilter;
import net.blay09.mods.refinedrelocation.filter.ModFilter;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HandlerLoginSyncList implements IMessageHandler<MessageLoginSyncList, IMessage> {

	@Override
	public IMessage onMessage(final MessageLoginSyncList message, MessageContext ctx) {
		RefinedRelocation.proxy.addScheduledTask(new Runnable() {
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
