package net.blay09.mods.refinedrelocation2.container;

import net.blay09.mods.refinedrelocation2.api.capability.ISortingInventory;
import net.blay09.mods.refinedrelocation2.api.capability.IFilterProvider;
import net.blay09.mods.refinedrelocation2.container.handler.IPriorityHandler;
import net.blay09.mods.refinedrelocation2.network.NetworkHandler;
import net.blay09.mods.refinedrelocation2.network.container.MessageContainerInteger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFilter extends Container implements IPriorityHandler {

    private final EntityPlayer entityPlayer;
    private final IFilterProvider provider;

    private int lastPriority;

    public ContainerFilter(EntityPlayer entityPlayer, IFilterProvider provider) {
        this.entityPlayer = entityPlayer;
        this.provider = provider;
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
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        if(!entityPlayer.worldObj.isRemote) {
            int priority = getPriority();
            if (lastPriority != priority) {
                NetworkHandler.instance.sendTo(new MessageContainerInteger("priority", priority), (EntityPlayerMP) entityPlayer);
            }
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
        if(provider instanceof ISortingInventory) {
            return ((ISortingInventory) provider).getPriority();
        }
        return 0;
    }

    @Override
    public void setPriority(int priority) {
        this.lastPriority = priority;
        if(provider instanceof ISortingInventory) {
            ((ISortingInventory) provider).setPriority(priority);
        }
    }

}
