package net.blay09.mods.refinedrelocation.capability;

import net.blay09.mods.refinedrelocation.api.filter.IMultiRootFilter;
import net.blay09.mods.refinedrelocation.filter.MultiRootFilter;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityMultiRootFilter {

    @CapabilityInject(IMultiRootFilter.class)
    public static Capability<IMultiRootFilter> CAPABILITY;

    public static void register() {
        CapabilityManager.INSTANCE.register(IMultiRootFilter.class, new Capability.IStorage<IMultiRootFilter>() {
            @Override
            public INBT writeNBT(Capability<IMultiRootFilter> capability, IMultiRootFilter instance, Direction side) {
                return new CompoundNBT();
            }

            @Override
            public void readNBT(Capability<IMultiRootFilter> capability, IMultiRootFilter instance, Direction side, INBT nbt) {
            }
        }, MultiRootFilter::new);
    }

}
