package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.filter.CreativeTabFilter;
import net.blay09.mods.refinedrelocation.filter.ModFilter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class LoginSyncHandler {

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		NetworkHandler.wrapper.sendTo(new MessageLoginSyncList(MessageLoginSyncList.TYPE_CREATIVETABS, CreativeTabFilter.creativeTabs), (EntityPlayerMP) event.player);
		NetworkHandler.wrapper.sendTo(new MessageLoginSyncList(MessageLoginSyncList.TYPE_MODS, ModFilter.modIds), (EntityPlayerMP) event.player);
	}

}
