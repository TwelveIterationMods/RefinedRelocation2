package net.blay09.mods.refinedrelocation.client;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.client.IFilterPreviewGui;
import net.blay09.mods.refinedrelocation.network.MessageFilterPreview;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = {Dist.CLIENT}, modid = RefinedRelocation.MOD_ID)
public class FilterPreviewHandler {

    private static byte[] slotStates;

    public static void setSlotStates(byte[] slotStates) {
        FilterPreviewHandler.slotStates = slotStates;
    }

    @SubscribeEvent
    public static void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (slotStates != null) {
            if (event.getGui() instanceof IFilterPreviewGui) {
                IFilterPreviewGui gui = (IFilterPreviewGui) event.getGui();
                EntityPlayer entityPlayer = Minecraft.getInstance().player;
                Container container = gui.getContainer();
                GlStateManager.pushMatrix();
                GlStateManager.translatef(0, 0, 300);
                for (Slot slot : container.inventorySlots) {
                    if (slot.inventory == entityPlayer.inventory) {
                        int guiLeft = gui.getLeft();
                        int guiTop = gui.getTop();
                        if (slotStates[slot.getSlotIndex()] == MessageFilterPreview.STATE_SUCCESS) {
                            Gui.drawRect(guiLeft + slot.xPos, guiTop + slot.yPos, guiLeft + slot.xPos + 16, guiTop + slot.yPos + 16, 0x5500FF00);
                        }
                    }
                }
                GlStateManager.popMatrix();
            } else {
                slotStates = null;
            }
        }
    }

}
