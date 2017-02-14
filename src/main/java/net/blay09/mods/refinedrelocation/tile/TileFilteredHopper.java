package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilitySimpleFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingGridMember;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingInventory;
import net.blay09.mods.refinedrelocation.util.TileWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileFilteredHopper extends TileFastHopper {
	private final ISortingInventory sortingInventory = Capabilities.SORTING_INVENTORY.getDefaultInstance();
	private final IRootFilter rootFilter = CapabilityRootFilter.CAPABILITY.getDefaultInstance();

	@Override
	protected ItemStackHandler createItemHandler() {
		return new ItemStackHandler(5) {
			@Override
			public ItemStack insertItem(int slot, ItemStack itemStack, boolean simulate) {
				assert sortingInventory != null;
				if(itemStack.isEmpty() || !sortingInventory.getFilter().passes(new TileWrapper(TileFilteredHopper.this), itemStack)) {
					return itemStack;
				}
				return super.insertItem(slot, itemStack, simulate);
			}
		};
	}

	@Override
	public String getName() {
		return hasCustomName() ? customName : "container.refinedrelocation:filtered_hopper";
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilitySortingInventory.CAPABILITY || capability == CapabilitySortingGridMember.CAPABILITY
				|| capability == CapabilityRootFilter.CAPABILITY || capability == CapabilitySimpleFilter.CAPABILITY
				|| super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if(capability == CapabilitySortingInventory.CAPABILITY || capability == CapabilitySortingGridMember.CAPABILITY) {
			return (T) sortingInventory;
		} else if(capability == CapabilityRootFilter.CAPABILITY || capability == CapabilitySimpleFilter.CAPABILITY) {
			return (T) rootFilter;
		}
		return super.getCapability(capability, facing);
	}
}
