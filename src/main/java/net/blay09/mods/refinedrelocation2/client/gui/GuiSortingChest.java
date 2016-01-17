package net.blay09.mods.refinedrelocation2.client.gui;

import net.blay09.mods.refinedrelocation2.client.gui.element.GuiButtonEditFilter;
import net.blay09.mods.refinedrelocation2.container.ContainerSortingChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiSortingChest extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation("textures/gui/container/generic_54.png");

    private final IInventory chestInventory;
    private final IInventory playerInventory;
    private int inventoryRows;

    public GuiSortingChest(EntityPlayer entityPlayer, IInventory chestInventory) {
        super(new ContainerSortingChest(entityPlayer, chestInventory));
        this.chestInventory = chestInventory;
        playerInventory = entityPlayer.inventory;
        allowUserInput = false;
        inventoryRows = chestInventory.getSizeInventory() / 9;
        ySize = 114 + inventoryRows * 18;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(new GuiButtonEditFilter(0, guiLeft + xSize - 20, guiTop + 4, true));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(texture);
        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        drawTexturedModalRect(left, top, 0, 0, xSize, inventoryRows * 18 + 17);
        drawTexturedModalRect(left, top + inventoryRows * 18 + 17, 0, 126, xSize, 96);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(chestInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        fontRendererObj.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
    }

}
