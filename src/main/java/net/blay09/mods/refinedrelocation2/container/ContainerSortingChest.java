package net.blay09.mods.refinedrelocation2.container;

import net.blay09.mods.refinedrelocation2.tile.TileSortingChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSortingChest extends Container {

    private final int numRows;
    private final TileSortingChest tileEntity;

    public ContainerSortingChest(EntityPlayer entityPlayer, TileSortingChest tileEntity) {
        this.tileEntity = tileEntity;
        IItemHandler itemHandler = tileEntity.getItemHandler();
        this.numRows = itemHandler.getSlots() / 9;
        int offsetY = (numRows - 4) * 18;

        tileEntity.openInventory(entityPlayer);

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new SlotItemHandler(itemHandler, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(entityPlayer.inventory, j + i * 9 + 9, 8 + j * 18, 103 + i * 18 + offsetY));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(entityPlayer.inventory, i, 8 + i * 18, 161 + offsetY));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return tileEntity.isUseableByPlayer(entityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int index) {
        ItemStack itemStack = null;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();
            if (index < numRows * 9) {
                if (!mergeItemStack(slotStack, numRows * 9, inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!mergeItemStack(slotStack, 0, numRows * 9, false)) {
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
    public void onContainerClosed(EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);
        tileEntity.closeInventory(entityPlayer);
    }

    public TileSortingChest getTileEntity() {
        return tileEntity;
    }
}
