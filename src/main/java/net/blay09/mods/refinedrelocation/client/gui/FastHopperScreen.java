package net.blay09.mods.refinedrelocation.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.refinedrelocation.client.gui.base.ModContainerScreen;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiOpenFilterButton;
import net.blay09.mods.refinedrelocation.container.ContainerFastHopper;
import net.blay09.mods.refinedrelocation.tile.FastHopperTileEntity;
import net.blay09.mods.refinedrelocation.tile.FilteredHopperTileEntity;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class FastHopperScreen extends ModContainerScreen<ContainerFastHopper> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/hopper.png");

    private final FastHopperTileEntity tileEntity;

    public FastHopperScreen(ContainerFastHopper container, PlayerInventory playerInventory, ITextComponent displayName) {
        super(container, playerInventory, displayName);
        this.tileEntity = container.getTileEntity();
        this.ySize = 133;
    }

    @Override
    public void init() {
        super.init();

        if (tileEntity instanceof FilteredHopperTileEntity) {
            addButton(new GuiOpenFilterButton(guiLeft + xSize - 20, guiTop + 4, tileEntity));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        minecraft.fontRenderer.drawString(getTitle().getFormattedText(), 8, 6, 4210752);
        minecraft.fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

}
