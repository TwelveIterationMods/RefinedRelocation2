package net.blay09.mods.refinedrelocation2.network;

import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.network.container.HandlerContainerAction;
import net.blay09.mods.refinedrelocation2.network.container.HandlerContainerInteger;
import net.blay09.mods.refinedrelocation2.network.container.MessageContainerAction;
import net.blay09.mods.refinedrelocation2.network.container.MessageContainerInteger;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {

    public static final SimpleNetworkWrapper instance = new SimpleNetworkWrapper(RefinedRelocation2.MOD_ID);

    public static void register() {
        instance.registerMessage(HandlerScrollIndex.class, MessageScrollIndex.class, 1, Side.SERVER);
        instance.registerMessage(HandlerOpenToolbox.class, MessageOpenToolbox.class, 2, Side.SERVER);
        instance.registerMessage(HandlerOpenFilter.class, MessageOpenFilter.class, 3, Side.SERVER);
        instance.registerMessage(HandlerContainerAction.class, MessageContainerAction.class, 4, Side.SERVER);
        instance.registerMessage(HandlerContainerInteger.class, MessageContainerInteger.class, 5, Side.SERVER);
        instance.registerMessage(HandlerContainerInteger.class, MessageContainerInteger.class, 6, Side.CLIENT);
    }

}
