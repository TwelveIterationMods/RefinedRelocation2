package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilitySimpleFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingGridMember;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingInventory;
import net.blay09.mods.refinedrelocation.filter.SameItemFilter;
import net.blay09.mods.refinedrelocation.network.GuiHandler;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@SuppressWarnings("unused")
public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {
		ModBlocks.init();
		ModItems.init();
		NetworkHandler.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(RefinedRelocation.instance, new GuiHandler());

		RefinedRelocationAPI.__internal__setupAPI(new InternalMethodsImpl());
		CapabilitySimpleFilter.register();
		CapabilityRootFilter.register();
		CapabilitySortingGridMember.register();
		CapabilitySortingInventory.register();

		RefinedRelocationAPI.registerFilter(SameItemFilter.ID, SameItemFilter.class);
	}

	public void init(FMLInitializationEvent event) {

	}


	public void postInit(FMLPostInitializationEvent event) {
		ModRecipes.init();
	}
}
