package net.blay09.mods.refinedrelocation.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.client.IFilterPreviewGui;
import net.blay09.mods.refinedrelocation.network.MessageFilterPreview;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
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
                PlayerEntity entityPlayer = Minecraft.getInstance().player;
                Container container = gui.getFilterContainer();
                RenderSystem.pushMatrix();
                RenderSystem.translatef(0, 0, 300);
                for (Slot slot : container.inventorySlots) {
                    if (slot.inventory == entityPlayer.inventory) {
                        int guiLeft = gui.getFilterGuiLeft();
                        int guiTop = gui.getFilterGuiTop();
                        if (slotStates[slot.getSlotIndex()] == MessageFilterPreview.STATE_SUCCESS) {
                            AbstractGui.fill(guiLeft + slot.xPos, guiTop + slot.yPos, guiLeft + slot.xPos + 16, guiTop + slot.yPos + 16, 0x5500FF00);
                        }
                    }
                }
                RenderSystem.popMatrix();
            } else {
                slotStates = null;
            }
        }
    }

}
