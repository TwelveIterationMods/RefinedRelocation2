package net.blay09.mods.refinedrelocation.client;

import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.refinedrelocation.SortingChestType;
import net.minecraft.client.renderer.Sheets;

public class ModTextures {

    public static void initialize(BalmTextures textures) {
        for (SortingChestType chestType : SortingChestType.values()) {
            textures.addSprite(Sheets.CHEST_SHEET, chestType.getTextureLocation());
        }
    }

}
