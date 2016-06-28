package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.network.MessageContainer;

public interface IContainerNetworked {
	 void receivedMessageClient(MessageContainer message);
	 void receivedMessageServer(MessageContainer message);
}
