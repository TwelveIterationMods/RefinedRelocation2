package net.blay09.mods.refinedrelocation.api.container;

public interface IContainerNetworked {
	 void receivedMessageClient(IContainerMessage message);
	 void receivedMessageServer(IContainerMessage message);
}
