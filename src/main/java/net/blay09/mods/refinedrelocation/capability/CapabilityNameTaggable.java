package net.blay09.mods.refinedrelocation.capability;

import com.google.common.base.Strings;
import net.blay09.mods.refinedrelocation.api.INameTaggable;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityNameTaggable {

    @CapabilityInject(INameTaggable.class)
    public static Capability<INameTaggable> CAPABILITY;

    public static void register() {
        CapabilityManager.INSTANCE.register(INameTaggable.class, new Capability.IStorage<INameTaggable>() {
            @Override
            public INBTBase writeNBT(Capability<INameTaggable> capability, INameTaggable instance, EnumFacing side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<INameTaggable> capability, INameTaggable instance, EnumFacing side, INBTBase nbt) {
                instance.deserializeNBT((NBTTagCompound) nbt);
            }
        }, () -> new INameTaggable() {
            private String customName = "";

            @Override
            public void setCustomName(String displayName) {
                customName = displayName;
            }

            @Override
            public String getCustomName() {
                return customName;
            }

            @Override
            public boolean hasCustomName() {
                return !Strings.isNullOrEmpty(customName);
            }
        });
    }

}
