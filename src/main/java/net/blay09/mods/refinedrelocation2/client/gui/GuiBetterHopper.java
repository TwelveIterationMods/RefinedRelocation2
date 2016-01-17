package net.blay09.mods.refinedrelocation2.client.gui;

import net.blay09.mods.refinedrelocation2.client.gui.element.GuiButtonEditFilter;
import net.blay09.mods.refinedrelocation2.container.ContainerFilteredHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiBetterHopper extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation("textures/gui/container/hopper.png");

    private final IInventory hopperInventory;
    private final IInventory playerInventory;
    private final boolean isFiltered;

    public GuiBetterHopper(EntityPlayer entityPlayer, IInventory hopperInventory, boolean isFiltered) {
        super(new ContainerFilteredHopper(entityPlayer, hopperInventory));
        this.hopperInventory = hopperInventory;
        this.isFiltered = isFiltered;
        playerInventory = entityPlayer.inventory;
        allowUserInput = false;
        ySize = 133;
    }

    @Override
    public void initGui() {
        super.initGui();
        if(isFiltered) {
            buttonList.add(new GuiButtonEditFilter(0, guiLeft + xSize - 22, guiTop + 5, false));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                Minecraft.getMinecraft().displayGuiScreen(new GuiFilter(null));
                break;
        }
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
