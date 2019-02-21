package net.blay09.mods.refinedrelocation.container;

import invtweaks.api.container.ChestContainer;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.blay09.mods.refinedrelocation.util.IContainerWithDoor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.SlotItemHandler;

@ChestContainer
public class ContainerSortingChest extends ContainerMod implements IContainerWithDoor {

    private final TileSortingChest tileEntity;

    public ContainerSortingChest(EntityPlayer player, TileSortingChest tileEntity) {
        this.tileEntity = tileEntity;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new SlotItemHandler(tileEntity.getItemHandler(), j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }

        addPlayerInventory(player, 85);

        tileEntity.getDoorAnimator().openContainer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();

            if (index < 27) {
                if (!mergeItemStack(slotStack, 27, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(slotStack, 0, 27, false)) {
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
    public boolean canInteractWith(EntityPlayer player) {
        return !tileEntity.isRemoved() && player.getDistanceSq(tileEntity.getPos().getX() + 0.5, tileEntity.getPos().getY() + 0.5, tileEntity.getPos().getZ() + 0.5) <= 64;
    }

    @Override
    public boolean isTileEntity(TileEntity tileEntity) {
        return this.tileEntity == tileEntity;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        tileEntity.getDoorAnimator().closeContainer(player);
    }

}
