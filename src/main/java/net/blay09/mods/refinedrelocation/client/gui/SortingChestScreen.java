package net.blay09.mods.refinedrelocation.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.refinedrelocation.SortingChestType;
import net.blay09.mods.refinedrelocation.client.gui.base.ModContainerScreen;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiOpenFilterButton;
import net.blay09.mods.refinedrelocation.container.SortingChestContainer;
import net.blay09.mods.refinedrelocation.tile.SortingChestTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class SortingChestScreen extends ModContainerScreen<SortingChestContainer> {

    private final SortingChestTileEntity tileEntity;

    public SortingChestScreen(SortingChestContainer container, PlayerInventory playerInventory, ITextComponent displayName) {
        super(container, playerInventory, displayName);
        this.tileEntity = container.getTileEntity();
        this.xSize = tileEntity.getChestType().getGuiWidth();
        this.ySize = tileEntity.getChestType().getGuiHeight();
    }

    @Override
    public void init() {
        super.init();

        addButton(new GuiOpenFilterButton(guiLeft + xSize - 20, guiTop + 4, tileEntity, 0));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        SortingChestType chestType = tileEntity.getChestType();
        Minecraft.getInstance().getTextureManager().bindTexture(chestType.getGuiTextureLocation());
        if (chestType == SortingChestType.WOOD || chestType == SortingChestType.IRON) {
            int inventoryRows = chestType.getInventorySize() / chestType.getContainerRowSize();
            blit(guiLeft, guiTop, 0, 0, xSize, inventoryRows * 18 + 17);
            blit(guiLeft, guiTop + inventoryRows * 18 + 17, 0, 125, xSize, 97);
        } else {
            int textureSizeX = chestType.getGuiTextureWidth();
            int textureSizeY = chestType.getGuiTextureHeight();
            blit(guiLeft, guiTop, 0, 0, xSize, ySize, textureSizeX, textureSizeY);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        font.drawString(getTitle().getFormattedText(), 8, 6, 4210752);

        int inventoryTitleX = (tileEntity.getChestType().getGuiWidth() - 162) / 2;
        font.drawString(I18n.format("container.inventory"), inventoryTitleX, ySize - 96 + 2, 4210752);
    }

}
