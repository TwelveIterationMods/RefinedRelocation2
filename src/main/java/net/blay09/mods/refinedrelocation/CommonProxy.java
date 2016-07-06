package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.api.container.ITileGuiHandler;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilitySimpleFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingGridMember;
import net.blay09.mods.refinedrelocation.capability.CapabilitySortingInventory;
import net.blay09.mods.refinedrelocation.filter.CreativeTabFilter;
import net.blay09.mods.refinedrelocation.filter.ModFilter;
import net.blay09.mods.refinedrelocation.filter.NameFilter;
import net.blay09.mods.refinedrelocation.filter.PresetFilter;
import net.blay09.mods.refinedrelocation.filter.SameItemFilter;
import net.blay09.mods.refinedrelocation.network.GuiHandler;
import net.blay09.mods.refinedrelocation.network.LoginSyncHandler;
import net.blay09.mods.refinedrelocation.network.MessageOpenGui;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@SuppressWarnings("unused")
public class CommonProxy {

	public final GuiHandler guiHandler = new GuiHandler();

	public void preInit(FMLPreInitializationEvent event) {
		ModBlocks.init();
		ModItems.init();
		NetworkHandler.init();
		MinecraftForge.EVENT_BUS.register(new LoginSyncHandler());

		RefinedRelocationAPI.__internal__setupAPI(new InternalMethodsImpl());
		CapabilitySimpleFilter.register();
		CapabilityRootFilter.register();
		CapabilitySortingGridMember.register();
		CapabilitySortingInventory.register();

		RefinedRelocationAPI.registerFilter(SameItemFilter.class);
		RefinedRelocationAPI.registerFilter(NameFilter.class);
		RefinedRelocationAPI.registerFilter(PresetFilter.class);
		RefinedRelocationAPI.registerFilter(CreativeTabFilter.class);
		RefinedRelocationAPI.registerFilter(ModFilter.class);

		RefinedRelocationAPI.registerGuiHandler(TileSortingChest.class, new ITileGuiHandler() {
			@Override
			public void openGui(EntityPlayer player, TileOrMultipart tileEntity) {
				RefinedRelocation.proxy.openGui(player, new MessageOpenGui(GuiHandler.GUI_SORTING_CHEST, tileEntity.getPos()));
			}
		});

	}

	public void init(FMLInitializationEvent event) {

	}

	public void postInit(FMLPostInitializationEvent event) {
		ModRecipes.init();

		CreativeTabFilter.gatherCreativeTabs();
		ModFilter.gatherMods();
	}

	public void addScheduledTask(Runnable runnable) {
		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(runnable);
	}

	public void openGui(EntityPlayer player, MessageOpenGui message) {
		if(player instanceof EntityPlayerMP) {
			EntityPlayerMP entityPlayer = (EntityPlayerMP) player;
			Container container = guiHandler.getContainer(message.getId(), entityPlayer, message);
			if (container != null) {
				entityPlayer.getNextWindowId();
				entityPlayer.closeContainer();
				int windowId = entityPlayer.currentWindowId;
				NetworkHandler.wrapper.sendTo(message.setWindowId(windowId), entityPlayer);
				entityPlayer.openContainer = container;
				entityPlayer.openContainer.windowId = windowId;
				entityPlayer.openContainer.addListener(entityPlayer);
			}
		}
	}

}
