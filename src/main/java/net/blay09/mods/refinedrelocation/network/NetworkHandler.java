package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {

    public static final SimpleChannel channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(RefinedRelocation.MOD_ID, "network"), () -> "1.0", it -> true, it -> true);

    public static void init() {
//        channel.registerMessage(0, MessageContainer.class, MessageContainer::encode, MessageContainer::decode, MessageContainer::handle);
        channel.registerMessage(1, MessageReturnGUI.class, (message, buf) -> {
        }, it -> new MessageReturnGUI(), MessageReturnGUI::handle);
        channel.registerMessage(2, MessageFilterPreview.class, MessageFilterPreview::encode, MessageFilterPreview::decode, MessageFilterPreview::handle);
        channel.registerMessage(3, MessageLoginSyncList.class, MessageLoginSyncList::encode, MessageLoginSyncList::decode, MessageLoginSyncList::handle);
    }

}
