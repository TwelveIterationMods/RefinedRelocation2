package net.blay09.mods.refinedrelocation.client;

import net.blay09.mods.refinedrelocation.api.client.IFilterPreviewGui;
import net.blay09.mods.refinedrelocation.network.MessageFilterPreview;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FilterPreviewHandler {

	private static byte[] slotStates;

	public static void setSlotStates(byte[] slotStates) {
		FilterPreviewHandler.slotStates = slotStates;
	}

	@SubscribeEvent
	public void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
		if(slotStates != null) {
			if (event.getGui() instanceof IFilterPreviewGui) {
				IFilterPreviewGui gui = (IFilterPreviewGui) event.getGui();
				EntityPlayer entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
				Container container = gui.getContainer();
				GlStateManager.pushMatrix();
				GlStateManager.translate(0, 0, 300);
				for (Slot slot : container.inventorySlots) {
					if (slot.inventory == entityPlayer.inventory) {
						int guiLeft = gui.getLeft();
						int guiTop = gui.getTop();
						if (slotStates[slot.getSlotIndex()] == MessageFilterPreview.STATE_SUCCESS) {
							Gui.drawRect(guiLeft + slot.xDisplayPosition, guiTop + slot.yDisplayPosition, guiLeft + slot.xDisplayPosition + 16, guiTop + slot.yDisplayPosition + 16, 0x5500FF00);
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
