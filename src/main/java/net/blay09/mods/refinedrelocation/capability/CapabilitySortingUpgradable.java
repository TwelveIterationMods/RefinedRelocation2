package net.blay09.mods.refinedrelocation.capability;

import net.blay09.mods.refinedrelocation.api.ISortingUpgradable;
import net.blay09.mods.refinedrelocation.api.SortingUpgradable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilitySortingUpgradable {

	@CapabilityInject(ISortingUpgradable.class)
	public static Capability<ISortingUpgradable> CAPABILITY;

	public static void register() {
		CapabilityManager.INSTANCE.register(ISortingUpgradable.class, new Capability.IStorage<ISortingUpgradable>() {
			@Override
			@Nullable
			public NBTBase writeNBT(Capability<ISortingUpgradable> capability, ISortingUpgradable instance, EnumFacing side) {
				return null;
			}
			@Override
			public void readNBT(Capability<ISortingUpgradable> capability, ISortingUpgradable instance, EnumFacing side, NBTBase nbt) {
			}
		}, SortingUpgradable.class);
	}

}
