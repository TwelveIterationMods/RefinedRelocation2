package net.blay09.mods.refinedrelocation;

import com.google.common.collect.Lists;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilitySimpleFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingGridMember;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingInventory;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingUpgradable;
import net.blay09.mods.refinedrelocation.client.render.RenderSortingChest;
import net.blay09.mods.refinedrelocation.compat.Compat;
import net.blay09.mods.refinedrelocation.compat.RefinedAddon;
import net.blay09.mods.refinedrelocation.filter.CreativeTabFilter;
import net.blay09.mods.refinedrelocation.filter.ModFilter;
import net.blay09.mods.refinedrelocation.filter.NameFilter;
import net.blay09.mods.refinedrelocation.filter.PresetFilter;
import net.blay09.mods.refinedrelocation.filter.SameItemFilter;
import net.blay09.mods.refinedrelocation.network.GuiHandler;
import net.blay09.mods.refinedrelocation.network.LoginSyncHandler;
import net.blay09.mods.refinedrelocation.network.MessageOpenGui;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod.EventBusSubscriber
@Mod(modid = RefinedRelocation.MOD_ID, name = "Refined Relocation", dependencies = "after:ironchest")
public class RefinedRelocation {

	public static final String MOD_ID = "refinedrelocation";

	public static final Logger logger = LogManager.getLogger(MOD_ID);

	@Mod.Instance(MOD_ID)
	public static RefinedRelocation instance;

	@SidedProxy(clientSide = "net.blay09.mods.refinedrelocation.client.ClientProxy", serverSide = "net.blay09.mods.refinedrelocation.CommonProxy")
	public static CommonProxy proxy;

	public static final CreativeTabs creativeTab = new CreativeTabs(MOD_ID) {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModBlocks.sortingChest);
		}
	};

	private static final List<RefinedAddon> inbuiltAddons = Lists.newArrayList();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		RefinedRelocationAPI.__internal__setupAPI(new InternalMethodsImpl());

		NetworkHandler.init();

		ModBlocks.registerTileEntities();

		MinecraftForge.EVENT_BUS.register(new LoginSyncHandler());

		CapabilitySimpleFilter.register();
		CapabilityRootFilter.register();
		CapabilitySortingGridMember.register();
		CapabilitySortingInventory.register();
		CapabilitySortingUpgradable.register();

		RefinedRelocationAPI.registerFilter(SameItemFilter.class);
		RefinedRelocationAPI.registerFilter(NameFilter.class);
		RefinedRelocationAPI.registerFilter(PresetFilter.class);
		RefinedRelocationAPI.registerFilter(CreativeTabFilter.class);
		RefinedRelocationAPI.registerFilter(ModFilter.class);

		RefinedRelocationAPI.registerGuiHandler(TileSortingChest.class, (player, tileEntity) -> RefinedRelocation.proxy.openGui(player, new MessageOpenGui(GuiHandler.GUI_SORTING_CHEST, tileEntity.getPos())));
		RefinedRelocationAPI.registerGuiHandler(TileBlockExtender.class, (player, tileEntity) -> RefinedRelocation.proxy.openGui(player, new MessageOpenGui(GuiHandler.GUI_BLOCK_EXTENDER, tileEntity.getPos())));

		if(Loader.isModLoaded(Compat.IRONCHEST)) {
			try {
				inbuiltAddons.add((RefinedAddon) Class.forName("net.blay09.mods.refinedrelocation.compat.ironchest.IronChestAddon").newInstance());
			} catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		for(RefinedAddon addon : inbuiltAddons) {
			addon.preInit();
		}

		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		for(RefinedAddon addon : inbuiltAddons) {
			addon.init();
		}

		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		CreativeTabFilter.gatherCreativeTabs();
		ModFilter.gatherMods();

		proxy.postInit(event);
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		ModBlocks.register(event.getRegistry());

		for(RefinedAddon addon : inbuiltAddons) {
			addon.registerBlocks(event.getRegistry());
		}
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		ModItems.register(event.getRegistry());
		ModBlocks.registerItemBlocks(event.getRegistry());

		for(RefinedAddon addon : inbuiltAddons) {
			addon.registerItems(event.getRegistry());
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent event) {
		ModBlocks.registerModels();
		ModItems.registerModels();

		for(RefinedAddon addon : inbuiltAddons) {
			addon.registerModels();
		}

		ClientRegistry.bindTileEntitySpecialRenderer(TileSortingChest.class, new RenderSortingChest());
	}

	public static List<RefinedAddon> getInbuiltAddons() {
		return inbuiltAddons;
	}
}
