package net.blay09.mods.refinedrelocation.container;

import invtweaks.api.container.ChestContainer;
import net.blay09.mods.refinedrelocation.SortingChestType;
import net.blay09.mods.refinedrelocation.tile.SortingChestTileEntity;
import net.blay09.mods.refinedrelocation.util.IContainerWithDoor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.SlotItemHandler;

@ChestContainer
public class SortingChestContainer extends BaseContainer implements IContainerWithDoor {

    private final SortingChestTileEntity tileEntity;

    public SortingChestContainer(int windowId, PlayerInventory playerInventory, SortingChestTileEntity tileEntity) {
        super(ModContainers.sortingChest, windowId);

        this.tileEntity = tileEntity;

        SortingChestType chestType = tileEntity.getChestType();
        int rowSize = chestType.getContainerRowSize();
        int rowCount = chestType.getInventorySize() / rowSize;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < rowSize; j++) {
                addSlot(new SlotItemHandler(tileEntity.getItemHandler(), j + i * rowSize, 8 + j * 18, 18 + i * 18));
            }
        }

        addPlayerInventory(playerInventory, (chestType.getGuiWidth() - 162) / 2 + 1, rowCount * 18 + 32);

        tileEntity.openChest(playerInventory.player);
    }

    public SortingChestTileEntity getTileEntity() {
        return tileEntity;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();

            int inventoryStartIndex = tileEntity.getChestType().getInventorySize();
            if (index < inventoryStartIndex) {
                if (!mergeItemStack(slotStack, inventoryStartIndex, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(slotStack, 0, inventoryStartIndex, false)) {
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

    @Override
    public boolean isTileEntity(TileEntity tileEntity) {
        return this.tileEntity == tileEntity;
    }

    @Override
    public void onContainerClosed(PlayerEntity player) {
        super.onContainerClosed(player);
        tileEntity.closeChest(player);
    }

}
