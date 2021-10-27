package net.blay09.mods.refinedrelocation.grid;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class SortingStack {

	private final IItemHandler itemHandler;
	private final int slotIndex;
	private final ItemStack itemStack;

	public SortingStack(IItemHandler itemHandler, int slotIndex, ItemStack itemStack) {
		this.itemHandler = itemHandler;
		this.slotIndex = slotIndex;
		this.itemStack = itemStack;
	}

	public IItemHandler getItemHandler() {
		return itemHandler;
	}

	public int getSlotIndex() {
		return slotIndex;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

}
