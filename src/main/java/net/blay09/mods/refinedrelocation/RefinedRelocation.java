package net.blay09.mods.refinedrelocation;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = RefinedRelocation.MOD_ID, name = "Refined Relocation", dependencies = "after:ironchest")
public class RefinedRelocation {

	// TODO test drops (hopper, block extender) on break --- FAST HOPPER DELETES ONLY SOME WHUT
	// TODO test on server

	public static final String MOD_ID = "refinedrelocation";

	@Mod.Instance(MOD_ID)
	public static RefinedRelocation instance;

	public static final Logger logger = LogManager.getLogger();

	@SidedProxy(clientSide = "net.blay09.mods.refinedrelocation.client.ClientProxy", serverSide = "net.blay09.mods.refinedrelocation.CommonProxy")
	public static CommonProxy proxy;

	public static final CreativeTabs creativeTab = new CreativeTabs(MOD_ID) {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModBlocks.sortingChest);
		}
	};

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

}
