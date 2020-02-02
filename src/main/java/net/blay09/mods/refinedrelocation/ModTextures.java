package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.client.render.SortingChestTileEntityRenderer;
import net.minecraft.client.renderer.Atlases;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RefinedRelocation.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTextures {

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().getBasePath().equals(Atlases.CHEST_ATLAS)) {
            event.addSprite(SortingChestTileEntityRenderer.TEXTURE);
        }
    }

}
