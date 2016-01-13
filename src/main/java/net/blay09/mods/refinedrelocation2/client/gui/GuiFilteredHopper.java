package net.blay09.mods.refinedrelocation2.client.gui;

import net.blay09.mods.refinedrelocation2.container.ContainerFilteredHopper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiFilteredHopper extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation("textures/gui/container/hopper.png");

    private final IInventory hopperInventory;
    private final IInventory playerInventory;

    public GuiFilteredHopper(EntityPlayer entityPlayer, IInventory hopperInventory) {
        super(new ContainerFilteredHopper(entityPlayer, hopperInventory));
        this.hopperInventory = hopperInventory;
        playerInventory = entityPlayer.inventory;
        allowUserInput = false;
        ySize = 133;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(texture);
        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(hopperInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        fontRendererObj.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
    }

}
