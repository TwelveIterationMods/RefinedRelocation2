package net.blay09.mods.refinedrelocation2.container;

import net.blay09.mods.refinedrelocation2.tile.TileBetterHopper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerFilteredHopper extends Container {

    private final TileBetterHopper tileEntity;

    public ContainerFilteredHopper(EntityPlayer entityPlayer, TileBetterHopper tileEntity) {
        this.tileEntity = tileEntity;
        IItemHandler itemHandler = tileEntity.getItemHandler();
        int offsetY = 51;

        for (int i = 0; i < itemHandler.getSlots(); i++) {
            this.addSlotToContainer(new SlotItemHandler(itemHandler, i, 44 + i * 18, 20));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(entityPlayer.inventory, j + i * 9 + 9, 8 + j * 18, i * 18 + offsetY));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(entityPlayer.inventory, i, 8 + i * 18, 58 + offsetY));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return tileEntity.isUseableByPlayer(entityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int index) {
        IItemHandler itemHandler = tileEntity.getItemHandler();
        ItemStack itemStack = null;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();
            if (index < itemHandler.getSlots()) {
                if (!mergeItemStack(slotStack, itemHandler.getSlots(), inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!mergeItemStack(slotStack, 0, itemHandler.getSlots(), false)) {
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

}
