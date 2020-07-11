package net.blay09.mods.refinedrelocation.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.refinedrelocation.client.gui.base.ModContainerScreen;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiOpenFilterButton;
import net.blay09.mods.refinedrelocation.container.ContainerFastHopper;
import net.blay09.mods.refinedrelocation.tile.FastHopperTileEntity;
import net.blay09.mods.refinedrelocation.tile.FilteredHopperTileEntity;
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
        field_238745_s_= ySize - 96 + 2;
    }

    @Override
    public void init() {
        super.init();

        if (tileEntity instanceof FilteredHopperTileEntity) {
            addButton(new GuiOpenFilterButton(guiLeft + xSize - 20, guiTop + 4, tileEntity, 0));
        }
    }

    @Override // drawGuiContainerBackgroundLayer
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);
    }

}
