package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.api.container.IContainerMessage;
import net.blay09.mods.refinedrelocation.api.container.IContainerNetworked;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;

import javax.annotation.Nullable;

public class ContainerMod extends Container implements IContainerNetworked {

    protected ContainerMod(@Nullable ContainerType<?> type, int windowId) {
        super(type, windowId);
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    @Override
    public void receivedMessageClient(IContainerMessage message) {

    }

    @Override
    public void receivedMessageServer(IContainerMessage message) {

    }

    protected void addPlayerInventory(PlayerInventory playerInventory, int offsetY) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, offsetY + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, offsetY + 58));
        }
    }

}
