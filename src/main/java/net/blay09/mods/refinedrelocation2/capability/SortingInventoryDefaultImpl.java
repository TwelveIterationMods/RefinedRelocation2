package net.blay09.mods.refinedrelocation2.capability;

import net.blay09.mods.refinedrelocation2.api.capability.ISortingInventory;
import net.blay09.mods.refinedrelocation2.api.filter.IFilter;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class SortingInventoryDefaultImpl extends SortingGridMemberDefaultImpl implements ISortingInventory {

    private IFilter filter;
    private IInventory inventory;

    @Override
    public void setInventory(IInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void setFilter(IFilter filter) {
        this.filter = filter;
    }

    @Override
    public IFilter getFilter() {
        return filter;
    }

    @Override
    public boolean putInInventory(ItemStack itemStack, boolean simulate) {
        if(simulate) {
            itemStack = itemStack.copy();
        }
        int firstEmptySlot = -1;
        for(int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack slotStack = inventory.getStackInSlot(i);
            if(slotStack != null) {
                if(ItemHandlerHelper.canItemStacksStack(itemStack, slotStack)) {
                    int spaceLeft = Math.min(inventory.getInventoryStackLimit(), slotStack.getMaxStackSize() - slotStack.stackSize);
                    if(spaceLeft > 0) {
                        ItemStack insertStack = itemStack.splitStack(Math.min(spaceLeft, itemStack.stackSize));
                        if(!simulate) {
                            slotStack.stackSize += insertStack.stackSize;
                        }
                        if(itemStack.stackSize == 0) {
                            return true;
                        }
                    }
                }
            } else {
                if(firstEmptySlot == -1 && inventory.isItemValidForSlot(i, itemStack)) {
                    firstEmptySlot = i;
                }
            }
        }
        if(firstEmptySlot != -1) {
            int spaceLeft = Math.min(inventory.getInventoryStackLimit(), itemStack.stackSize);
            if(spaceLeft > 0) {
                ItemStack insertStack = itemStack.splitStack(spaceLeft);
                if(!simulate) {
                    inventory.setInventorySlotContents(firstEmptySlot, insertStack);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void slotChanged(int slotIndex) {

    }

}
