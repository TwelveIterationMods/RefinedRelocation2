package net.blay09.mods.refinedrelocation.api.container;

public interface IContainerNetworked {
	 void receivedMessageClient(IMessageContainer message);
	 void receivedMessageServer(IMessageContainer message);
}
