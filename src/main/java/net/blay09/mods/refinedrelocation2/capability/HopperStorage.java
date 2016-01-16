package net.blay09.mods.refinedrelocation2.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class HopperStorage implements Capability.IStorage<IHopper> {
    @Override
    public NBTBase writeNBT(Capability<IHopper> capability, IHopper instance, EnumFacing side) {
        return null;
    }

    @Override
    public void readNBT(Capability<IHopper> capability, IHopper instance, EnumFacing side, NBTBase nbt) {

    }
}
