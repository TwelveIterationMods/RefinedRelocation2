package net.blay09.mods.refinedrelocation.capability;

import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.grid.SortingInventory;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilitySortingInventory {

	@CapabilityInject(ISortingInventory.class)
	public static Capability<ISortingInventory> CAPABILITY;

	public static void register() {
		CapabilityManager.INSTANCE.register(ISortingInventory.class, new Capability.IStorage<ISortingInventory>() {
			@Override
			public INBTBase writeNBT(Capability<ISortingInventory> capability, ISortingInventory instance, EnumFacing side) {
				NBTTagCompound compound = new NBTTagCompound();
				compound.setShort("Priority", (short) instance.getPriority());
				return compound;
			}

			@Override
			public void readNBT(Capability<ISortingInventory> capability, ISortingInventory instance, EnumFacing side, INBTBase nbt) {
				NBTTagCompound compound = (NBTTagCompound) nbt;
				instance.setPriority(compound.getShort("Priority"));
			}
		}, SortingInventory::new);
	}

}
