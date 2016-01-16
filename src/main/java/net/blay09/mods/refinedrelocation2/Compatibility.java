package net.blay09.mods.refinedrelocation2;

import com.google.common.collect.Lists;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class Compatibility {

    public static final List<Capability> blockExtenderConnections = Lists.newArrayList();

    public static void postInit() {
        blockExtenderConnections.add(RefinedRelocation2.HOPPER);

        MinecraftForge.EVENT_BUS.register(new Compatibility());
    }

    @SubscribeEvent
    public void onTileCapabilities(AttachCapabilitiesEvent.TileEntity event) {
        if(event.getTileEntity() instanceof TileEntityHopper) {
            event.addCapability(new ResourceLocation(RefinedRelocation2.MOD_ID, "hopper"), new ICapabilityProvider() {
                @Override
                public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
                    return true;
                }

                @Override
                @SuppressWarnings("unchecked")
                public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
                    return (T) RefinedRelocation2.HOPPER.getDefaultInstance();
                }
            });
        }
    }

}
