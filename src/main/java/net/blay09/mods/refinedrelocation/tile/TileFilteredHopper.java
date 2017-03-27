package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilitySimpleFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingGridMember;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileFilteredHopper extends TileFastHopper {
	private final IRootFilter rootFilter = Capabilities.getDefaultInstance(Capabilities.ROOT_FILTER);

	@Override
	protected ItemStackHandler createItemHandler() {
		return new ItemStackHandler(5) {
			@Override
			public ItemStack insertItem(int slot, ItemStack itemStack, boolean simulate) {
				if(itemStack.isEmpty() || !rootFilter.passes(TileFilteredHopper.this, itemStack)) {
					return itemStack;
				}
				return super.insertItem(slot, itemStack, simulate);
			}
		};
	}

	@Override
	public String getUnlocalizedName() {
		return "container.refinedrelocation:filtered_hopper";
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilitySortingGridMember.CAPABILITY
				|| capability == CapabilityRootFilter.CAPABILITY || capability == CapabilitySimpleFilter.CAPABILITY
				|| super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if(capability == CapabilityRootFilter.CAPABILITY || capability == CapabilitySimpleFilter.CAPABILITY) {
			return (T) rootFilter;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setTag("RootFilter", rootFilter.serializeNBT());
		return tagCompound;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		rootFilter.deserializeNBT(tagCompound.getCompoundTag("RootFilter"));
	}

}
