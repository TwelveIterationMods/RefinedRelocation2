package net.blay09.mods.refinedrelocation2.container;

import net.blay09.mods.refinedrelocation2.api.capability.ISortingInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFilter extends Container implements IPriorityHandler {

    private int priority;

    public ContainerFilter(EntityPlayer entityPlayer, ISortingInventory sortingInventory) {
        addSlotToContainer(new Slot(entityPlayer.inventory, 0, 8, 29)); // this should not be entityPlayer.inventory

        int inventoryY = 128;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(entityPlayer.inventory, j + i * 9 + 9, 8 + j * 18, i * 18 + inventoryY));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(entityPlayer.inventory, i, 8 + i * 18, 58 + inventoryY));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int index) {
        ItemStack itemStack = null;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();
            if (index >= 28) {
                if (!mergeItemStack(slotStack, 1, 28, true)) {
                    return null;
                }
            } else if (!mergeItemStack(slotStack, 28, 37, false)) {
                return null;
            }

            if (slotStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemStack;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

}
