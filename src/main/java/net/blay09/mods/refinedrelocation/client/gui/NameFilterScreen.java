package net.blay09.mods.refinedrelocation.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.client.IFilterPreviewGui;
import net.blay09.mods.refinedrelocation.client.gui.base.GuiContainerMod;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollBar;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollPane;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiTextFieldMultiLine;
import net.blay09.mods.refinedrelocation.container.ContainerNameFilter;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class NameFilterScreen extends GuiContainerMod<ContainerNameFilter> implements IFilterPreviewGui {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/root_filter.png");

    private static final int UPDATE_INTERVAL = 20;

    private GuiTextFieldMultiLine txtFilter;

    private int ticksSinceUpdate;
    private String lastSentText = "";

    public NameFilterScreen(ContainerNameFilter container, PlayerInventory playerInventory, ITextComponent displayName) {
        super(container, playerInventory, displayName);

        ySize = 210;
        shouldKeyRepeat = true;
    }

    @Override
    public void init() {
        super.init();

        txtFilter = new GuiTextFieldMultiLine(guiLeft + 8, guiTop + 20, 150, 84);
        txtFilter.changeFocus(true);
        txtFilter.setCanLoseFocus(false);
        txtFilter.setText(container.getValue());

        GuiScrollBar scrollBar = new GuiScrollBar(guiLeft + 161, guiTop + 20, 83, txtFilter);

        GuiScrollPane scrollPane = new GuiScrollPane(scrollBar, guiLeft + 8, guiTop + 20, 150, 84);
        children.add(scrollPane);
        children.add(txtFilter);

        addButton(scrollBar);
    }

    @Override
    public void tick() {
        super.tick();

        // Sync from Server
        if (container.doesGuiNeedUpdate()) {
            txtFilter.setText(container.getValue());
            lastSentText = container.getValue();
            container.markGuiNeedsUpdate(false);
        }

        // Sync to Server
        ticksSinceUpdate++;
        if (ticksSinceUpdate >= UPDATE_INTERVAL) {
            if (!lastSentText.equals(txtFilter.getText())) {
                container.sendValueToServer(txtFilter.getText());
                lastSentText = txtFilter.getText();
            }
            ticksSinceUpdate = 0;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        minecraft.getTextureManager().bindTexture(TEXTURE);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        minecraft.fontRenderer.drawString(I18n.format("container.refinedrelocation:name_filter"), 8, 6, 4210752);
        minecraft.fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    public boolean onGuiAboutToClose() {
        super.onGuiAboutToClose();
        container.sendValueToServer(txtFilter.getText());
        RefinedRelocationAPI.returnToParentContainer();
        return false;
    }

    @Override
    public int getLeft() {
        return guiLeft;
    }

    @Override
    public int getTop() {
        return guiTop;
    }
}
