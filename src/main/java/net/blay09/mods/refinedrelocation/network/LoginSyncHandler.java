package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.filter.CreativeTabFilter;
import net.blay09.mods.refinedrelocation.filter.ModFilter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class LoginSyncHandler {

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        NetworkHandler.channel.send(PacketDistributor.PLAYER.with(() -> (EntityPlayerMP) event.getPlayer()), new MessageLoginSyncList(MessageLoginSyncList.TYPE_CREATIVE_TABS, CreativeTabFilter.creativeTabs));
        NetworkHandler.channel.send(PacketDistributor.PLAYER.with(() -> (EntityPlayerMP) event.getPlayer()), new MessageLoginSyncList(MessageLoginSyncList.TYPE_MODS, ModFilter.modIds));
    }

}
