package net.blay09.mods.refinedrelocation2.capability;

import net.blay09.mods.refinedrelocation2.api.capability.ISortingGridMember;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class SortingMemberStorage implements Capability.IStorage<ISortingGridMember> {
    @Override
    public NBTBase writeNBT(Capability<ISortingGridMember> capability, ISortingGridMember instance, EnumFacing side) {
        return null;
    }

    @Override
    public void readNBT(Capability<ISortingGridMember> capability, ISortingGridMember instance, EnumFacing side, NBTBase nbt) {

    }
}
