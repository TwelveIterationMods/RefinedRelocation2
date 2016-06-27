package net.blay09.mods.refinedrelocation.grid;

import com.google.common.collect.Lists;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.filter.ISimpleFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.blay09.mods.refinedrelocation.util.ItemHandlerHelper2;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.LinkedList;

public class SortingInventory extends SortingGridMember implements ISortingInventory {

	private final LinkedList<SortingStack> sortingStackList = Lists.newLinkedList();
	private IItemHandler itemHandler;
	private ISimpleFilter filter;
	private int priority;

	@Override
	public IItemHandler getItemHandler() {
		return itemHandler;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public ISimpleFilter getFilter() {
		return filter;
	}

	@Override
	protected void onFirstTick() {
		super.onFirstTick();
		itemHandler = getGridContainer().getTileEntity().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		filter = getGridContainer().getTileEntity().getCapability(CapabilityRootFilter.CAPABILITY, null);
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		if(!sortingStackList.isEmpty()) {
			SortingStack sortingStack = sortingStackList.removeFirst();
			getSortingGrid().setSortingActive(true);
			ItemStack itemStack = sortingStack.getItemHandler().getStackInSlot(sortingStack.getSlotIndex());
			if(ItemStack.areItemStacksEqual(itemStack, sortingStack.getItemStack()) && ItemStack.areItemStackTagsEqual(itemStack, sortingStack.getItemStack())) {
				RefinedRelocationAPI.insertIntoSortingGrid(this, sortingStack.getSlotIndex(), itemStack);
			}
			getSortingGrid().setSortingActive(false);
		}
	}

	@Override
	public void onSlotChanged(int slotIndex) {
		if(!getSortingGrid().isSortingActive() && !getGridContainer().getWorld().isRemote) {
			ItemStack itemStack = itemHandler.getStackInSlot(slotIndex);
			if(itemStack != null) {
				sortingStackList.add(new SortingStack(itemHandler, slotIndex, itemStack));
			}
		}
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setShort("Priority", (short) priority);
		return compound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound compound) {
		priority = compound.getShort("Priority");
	}
}
