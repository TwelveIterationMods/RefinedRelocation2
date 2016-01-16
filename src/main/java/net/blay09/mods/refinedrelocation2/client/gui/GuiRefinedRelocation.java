package net.blay09.mods.refinedrelocation2.client.gui;

import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.balyware.TextureAtlas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiRefinedRelocation extends GuiScreen {

    public static final ResourceLocation atlasLocation = new ResourceLocation("textures/atlas/gui.png");
    public static TextureAtlas textureMap;
    public static void init() {
        textureMap = new TextureAtlas(new ResourceLocation(RefinedRelocation2.MOD_ID, "textures/gui/atlas.json"), "textures/gui");
        Minecraft.getMinecraft().renderEngine.loadTickableTexture(atlasLocation, textureMap);
    }

}
