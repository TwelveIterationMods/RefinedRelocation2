package net.blay09.mods.refinedrelocation.capability;

import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.filter.RootFilter;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityRootFilter {

	@CapabilityInject(IRootFilter.class)
	public static Capability<IRootFilter> CAPABILITY;

	public static void register() {
		CapabilityManager.INSTANCE.register(IRootFilter.class, new Capability.IStorage<IRootFilter>() {
			@Override
			public INBT writeNBT(Capability<IRootFilter> capability, IRootFilter instance, Direction side) {
				return instance.serializeNBT();
			}

			@Override
			public void readNBT(Capability<IRootFilter> capability, IRootFilter instance, Direction side, INBT nbt) {
				instance.deserializeNBT(nbt);
			}
		}, RootFilter::new);
	}

}
