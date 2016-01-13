package net.blay09.mods.refinedrelocation2.capability;

import net.blay09.mods.refinedrelocation2.api.capability.ISortingInventory;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class SortingInventoryStorage implements Capability.IStorage<ISortingInventory> {
    @Override
    public NBTBase writeNBT(Capability<ISortingInventory> capability, ISortingInventory instance, EnumFacing side) {
        return null;
    }

    @Override
    public void readNBT(Capability<ISortingInventory> capability, ISortingInventory instance, EnumFacing side, NBTBase nbt) {

    }
}
