package net.blay09.mods.refinedrelocation2;

import net.blay09.mods.refinedrelocation2.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingGridMember;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingInventory;
import net.blay09.mods.refinedrelocation2.capability.SortingGridMemberDefaultImpl;
import net.blay09.mods.refinedrelocation2.capability.SortingInventoryDefaultImpl;
import net.blay09.mods.refinedrelocation2.capability.SortingInventoryStorage;
import net.blay09.mods.refinedrelocation2.capability.SortingMemberStorage;
import net.blay09.mods.refinedrelocation2.network.GuiHandler;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(ISortingGridMember.class, new SortingMemberStorage(), SortingGridMemberDefaultImpl.class);
        CapabilityManager.INSTANCE.register(ISortingInventory.class, new SortingInventoryStorage(), SortingInventoryDefaultImpl.class);

        RefinedRelocationAPI.setupAPI(new InternalMethods());

        ModBlocks.register();
        ModItems.register();
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

}
