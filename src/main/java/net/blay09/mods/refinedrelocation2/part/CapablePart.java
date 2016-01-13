package net.blay09.mods.refinedrelocation2.part;

import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.Multipart;
import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public abstract class CapablePart extends Multipart implements ICapabilityProvider {

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return getCapability(capability, facing) != null;
    }

    public static boolean hasCapability(IMultipartContainer partContainer, Capability capability, EnumFacing side) {
        for(IMultipart sidePart : partContainer.getParts()) {
            if(sidePart instanceof ICapabilityProvider) {
                if(((ICapabilityProvider) sidePart).hasCapability(capability, side)) {
                    return true;
                }
            }
        }
        return false;
    }

}
