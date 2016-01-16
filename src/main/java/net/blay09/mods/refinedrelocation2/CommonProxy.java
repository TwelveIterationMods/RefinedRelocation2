package net.blay09.mods.refinedrelocation2;

import net.blay09.mods.refinedrelocation2.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingGridMember;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingInventory;
import net.blay09.mods.refinedrelocation2.capability.*;
import net.blay09.mods.refinedrelocation2.network.GuiHandler;
import net.blay09.mods.refinedrelocation2.network.NetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(ISortingGridMember.class, new SortingMemberStorage(), SortingGridMemberDefaultImpl.class);
        CapabilityManager.INSTANCE.register(ISortingInventory.class, new SortingInventoryStorage(), SortingInventoryDefaultImpl.class);
        CapabilityManager.INSTANCE.register(IHopper.class, new HopperStorage(), HopperDefaultImpl.class);

        RefinedRelocationAPI.setupAPI(new InternalMethods());

        ModBlocks.register();
        ModItems.register();

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(RefinedRelocation2.instance, new GuiHandler());
        NetworkHandler.register();
    }

    public void postInit(FMLPostInitializationEvent event) {
        Compatibility.postInit();
    }

    public void addScheduledTask(Runnable runnable) {
        MinecraftServer.getServer().addScheduledTask(runnable);
    }

    public void showItemHighlight() {}

    public boolean isTESRItem(ItemStack itemStack) {
        return false;
    }

}
