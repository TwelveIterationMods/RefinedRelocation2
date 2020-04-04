package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.tile.FastHopperTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFastHopper extends BaseContainer {

    private final FastHopperTileEntity tileEntity;

    public ContainerFastHopper(int windowId, PlayerInventory player, FastHopperTileEntity tileEntity) {
        super(ModContainers.fastHopper, windowId);

        this.tileEntity = tileEntity;

        for (int i = 0; i < 5; i++) {
            addSlot(new SlotItemHandlerCareless(tileEntity.getItemHandler(), i, 44 + i * 18, 20));
        }

        addPlayerInventory(player, 51);
    }

    public FastHopperTileEntity getTileEntity() {
        return tileEntity;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();

            if (index < 5) {
                if (!mergeItemStack(slotStack, 5, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(slotStack, 0, 5, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemStack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return !tileEntity.isRemoved() && player.getDistanceSq(tileEntity.getPos().getX() + 0.5, tileEntity.getPos().getY() + 0.5, tileEntity.getPos().getZ() + 0.5) <= 64;
    }

}
