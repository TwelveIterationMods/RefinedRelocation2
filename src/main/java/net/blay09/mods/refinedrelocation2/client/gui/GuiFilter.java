package net.blay09.mods.refinedrelocation2.client.gui;

import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingInventory;
import net.blay09.mods.refinedrelocation2.client.gui.element.GuiButtonPriority;
import net.blay09.mods.refinedrelocation2.container.ContainerFilter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiFilter extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(RefinedRelocation2.MOD_ID, "textures/gui/filter.png");

    private final IInventory playerInventory;

    public GuiFilter(EntityPlayer entityPlayer, ISortingInventory rootFilterProvider) {
        super(new ContainerFilter(entityPlayer, rootFilterProvider));
        playerInventory = entityPlayer.inventory;

        ySize = 209;
    }

    @Override
    public void initGui() {
        super.initGui();

        buttonList.add(new GuiButtonPriority(0, guiLeft + 30, guiTop + 29));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Setup Filter", 8, 8, 4210752);
        fontRendererObj.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);

        drawCenteredString(fontRendererObj, "No Filter", 40, 54, 14737632);
        drawCenteredString(fontRendererObj, "No Filter", 40, 74, 14737632);
        drawCenteredString(fontRendererObj, "No Filter", 40, 94, 14737632);
    }

}
