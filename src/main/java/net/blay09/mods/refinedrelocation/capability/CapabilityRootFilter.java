package net.blay09.mods.refinedrelocation.capability;

import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.filter.RootFilter;
import net.minecraft.nbt.INBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityRootFilter {

	@CapabilityInject(IRootFilter.class)
	public static Capability<IRootFilter> CAPABILITY;

	public static void register() {
		CapabilityManager.INSTANCE.register(IRootFilter.class, new Capability.IStorage<IRootFilter>() {
			@Override
			public INBTBase writeNBT(Capability<IRootFilter> capability, IRootFilter instance, EnumFacing side) {
				return instance.serializeNBT();
			}

			@Override
			public void readNBT(Capability<IRootFilter> capability, IRootFilter instance, EnumFacing side, INBTBase nbt) {
				instance.deserializeNBT(nbt);
			}
		}, RootFilter::new);
	}

}
