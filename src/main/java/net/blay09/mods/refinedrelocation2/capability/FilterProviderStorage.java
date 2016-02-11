package net.blay09.mods.refinedrelocation2.capability;

import net.blay09.mods.refinedrelocation2.api.capability.IFilterProvider;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class FilterProviderStorage implements Capability.IStorage<IFilterProvider> {
    @Override
    public NBTBase writeNBT(Capability<IFilterProvider> capability, IFilterProvider instance, EnumFacing side) {
        return null;
    }

    @Override
    public void readNBT(Capability<IFilterProvider> capability, IFilterProvider instance, EnumFacing side, NBTBase nbt) {

    }
}
