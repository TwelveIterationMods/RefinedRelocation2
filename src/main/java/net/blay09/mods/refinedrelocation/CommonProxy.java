package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.network.GuiHandler;
import net.blay09.mods.refinedrelocation.network.MessageOpenGui;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
	}

	public void init(FMLInitializationEvent event) {
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	public void openGui(EntityPlayer player, MessageOpenGui message) {
		if(player instanceof EntityPlayerMP) {
			EntityPlayerMP entityPlayer = (EntityPlayerMP) player;
			Container container = GuiHandler.getContainer(message.getId(), entityPlayer, message);
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
