package net.blay09.mods.refinedrelocation2.container;

public interface IContainerNetworked {
    default void receiveAction(String name) {}
    default void receiveInteger(String name, int value) {}
}
