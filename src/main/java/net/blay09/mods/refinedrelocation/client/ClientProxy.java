package net.blay09.mods.refinedrelocation.client;

import net.blay09.mods.refinedrelocation.CommonProxy;
import net.blay09.mods.refinedrelocation.ModBlocks;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.client.render.RenderSortingChest;
import net.blay09.mods.refinedrelocation.client.util.TextureAtlas;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.IOException;

public class ClientProxy extends CommonProxy {

	public static final TextureAtlas TEXTURE_ATLAS = new TextureAtlas(new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/atlas.json"), "textures/gui/");

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);

		ClientRegistry.bindTileEntitySpecialRenderer(TileSortingChest.class, new RenderSortingChest());
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.sortingChest), 0, TileSortingChest.class);

		MinecraftForge.EVENT_BUS.register(new BlockHighlightHandler());
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

		IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
		if(resourceManager instanceof IReloadableResourceManager) {
			((IReloadableResourceManager) resourceManager).registerReloadListener(TEXTURE_ATLAS);
		} else {
			try {
				TEXTURE_ATLAS.loadTexture(resourceManager);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
