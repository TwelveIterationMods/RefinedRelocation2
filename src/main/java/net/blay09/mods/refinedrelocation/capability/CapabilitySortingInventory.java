package net.blay09.mods.refinedrelocation.capability;

import net.blay09.mods.refinedrelocation.api.grid.ISortingInventory;
import net.blay09.mods.refinedrelocation.grid.SortingInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilitySortingInventory {

	@CapabilityInject(ISortingInventory.class)
	public static Capability<ISortingInventory> CAPABILITY;

	public static void register() {
		CapabilityManager.INSTANCE.register(ISortingInventory.class, new Capability.IStorage<ISortingInventory>() {
			@Override
			public INBT writeNBT(Capability<ISortingInventory> capability, ISortingInventory instance, Direction side) {
				CompoundNBT compound = new CompoundNBT();
				compound.putShort("Priority", (short) instance.getPriority());
				return compound;
			}

			@Override
			public void readNBT(Capability<ISortingInventory> capability, ISortingInventory instance, Direction side, INBT nbt) {
				CompoundNBT compound = (CompoundNBT) nbt;
				instance.setPriority(compound.getShort("Priority"));
			}
		}, SortingInventory::new);
	}

}
