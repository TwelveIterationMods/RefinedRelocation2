package net.blay09.mods.refinedrelocation.capability;

import net.blay09.mods.refinedrelocation.api.grid.ISortingGridMember;
import net.blay09.mods.refinedrelocation.grid.SortingGridMember;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilitySortingGridMember {

    @CapabilityInject(ISortingGridMember.class)
    public static Capability<ISortingGridMember> CAPABILITY;

    public static void register() {
        CapabilityManager.INSTANCE.register(ISortingGridMember.class, new Capability.IStorage<ISortingGridMember>() {
            @Override
            @Nullable
            public INBT writeNBT(Capability<ISortingGridMember> capability, ISortingGridMember instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<ISortingGridMember> capability, ISortingGridMember instance, Direction side, INBT nbt) {

            }
        }, SortingGridMember::new);
    }

}
