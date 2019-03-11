package net.blay09.mods.refinedrelocation.network;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {

    public static final SimpleChannel channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(RefinedRelocation.MOD_ID, "network"), () -> "1.0", it -> true, it -> true);

    public static void init() {
        channel.registerMessage(0, MessageReturnGUI.class, (message, buf) -> {
        }, it -> new MessageReturnGUI(), MessageReturnGUI::handle);
        channel.registerMessage(1, MessageFilterPreview.class, MessageFilterPreview::encode, MessageFilterPreview::decode, MessageFilterPreview::handle);
        channel.registerMessage(2, MessageLoginSyncList.class, MessageLoginSyncList::encode, MessageLoginSyncList::decode, MessageLoginSyncList::handle);
        channel.registerMessage(3, MessageRequestFilterGUI.class, MessageRequestFilterGUI::encode, MessageRequestFilterGUI::decode, MessageRequestFilterGUI::handle);
        channel.registerMessage(4, MessageContainerInt.class, MessageContainerInt::encode, MessageContainerInt::decode, MessageContainerInt::handle);
        channel.registerMessage(5, MessageContainerString.class, MessageContainerString::encode, MessageContainerString::decode, MessageContainerString::handle);
        channel.registerMessage(6, MessageContainerByteArray.class, MessageContainerByteArray::encode, MessageContainerByteArray::decode, MessageContainerByteArray::handle);
        channel.registerMessage(7, MessageContainerNBT.class, MessageContainerNBT::encode, MessageContainerNBT::decode, MessageContainerNBT::handle);
        channel.registerMessage(8, MessageContainerIndexedInt.class, MessageContainerIndexedInt::encode, MessageContainerIndexedInt::decode, MessageContainerIndexedInt::handle);
    }

}
