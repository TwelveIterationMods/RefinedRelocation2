package net.blay09.mods.refinedrelocation.client;

import net.blay09.mods.refinedrelocation.CommonProxy;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.client.render.RenderSortingChest;
import net.blay09.mods.refinedrelocation.client.util.TextureAtlas;
import net.blay09.mods.refinedrelocation.compat.RefinedAddon;
import net.blay09.mods.refinedrelocation.network.GuiHandler;
import net.blay09.mods.refinedrelocation.network.MessageOpenGui;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.IOException;

public class ClientProxy extends CommonProxy {

	public static final TextureAtlas TEXTURE_ATLAS = new TextureAtlas(new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/atlas.json"), "textures/gui");

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);

		MinecraftForge.EVENT_BUS.register(new BlockHighlightHandler());
		MinecraftForge.EVENT_BUS.register(new FilterPreviewHandler());
		MinecraftForge.EVENT_BUS.register(new OpenFilterButtonHandler());

		for(RefinedAddon addon : RefinedRelocation.getInbuiltAddons()) {
			addon.preInitClient();
		}
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

		IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
		if (resourceManager instanceof IReloadableResourceManager) {
			((IReloadableResourceManager) resourceManager).registerReloadListener(TEXTURE_ATLAS);
		} else {
			try {
				TEXTURE_ATLAS.loadTexture(resourceManager);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void openGui(EntityPlayer player, MessageOpenGui message) {
		super.openGui(player, message);
		if (player instanceof EntityPlayerSP) {
			GuiScreen guiScreen = GuiHandler.getGuiScreen(message.getId(), player, message);
			if (guiScreen != null) {
				Minecraft.getMinecraft().displayGuiScreen(guiScreen);
				player.openContainer.windowId = message.getWindowId();
			}
		}
	}
}
