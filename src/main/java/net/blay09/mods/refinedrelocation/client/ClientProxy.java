package net.blay09.mods.refinedrelocation.client;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.client.util.TextureAtlas;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class ClientProxy {

    public static final TextureAtlas TEXTURE_ATLAS = new TextureAtlas(new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/atlas.json"), "textures/gui");

    // TODO not yet called
    public void init() {
        IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof IReloadableResourceManager) {
            ((IReloadableResourceManager) resourceManager).addReloadListener(TEXTURE_ATLAS);
        } else {
            try {
                TEXTURE_ATLAS.loadTexture(resourceManager);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
