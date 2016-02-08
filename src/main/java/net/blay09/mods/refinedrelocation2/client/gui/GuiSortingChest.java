package net.blay09.mods.refinedrelocation2.client.gui;

import net.blay09.mods.refinedrelocation2.api.gui.IRootFilterGui;
import net.blay09.mods.refinedrelocation2.container.ContainerSortingChest;
import net.blay09.mods.refinedrelocation2.network.MessageOpenFilter;
import net.blay09.mods.refinedrelocation2.network.NetworkHandler;
import net.blay09.mods.refinedrelocation2.tile.TileSortingChest;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiSortingChest extends GuiContainer implements IRootFilterGui {

    private static final ResourceLocation texture = new ResourceLocation("textures/gui/container/generic_54.png");

    private final TileSortingChest tileEntity;
    private final IInventory playerInventory;
    private int inventoryRows;

    public GuiSortingChest(EntityPlayer entityPlayer, TileSortingChest tileEntity) {
        super(new ContainerSortingChest(entityPlayer, tileEntity));
        this.tileEntity = tileEntity;
        playerInventory = entityPlayer.inventory;
        allowUserInput = false;
        inventoryRows = tileEntity.getItemHandler().getSlots() / 9;
        ySize = 114 + inventoryRows * 18;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if(button.id == 0) {
            NetworkHandler.instance.sendToServer(new MessageOpenFilter());
        }
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
        fontRendererObj.drawString(tileEntity.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        fontRendererObj.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    public BlockPos getBlockPos() {
        return tileEntity.getPos();
    }
}
