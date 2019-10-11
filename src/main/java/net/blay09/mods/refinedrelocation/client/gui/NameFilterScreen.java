package net.blay09.mods.refinedrelocation.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.client.IFilterPreviewGui;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollBar;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollPane;
import net.blay09.mods.refinedrelocation.client.gui.base.element.MultiLineTextFieldWidget;
import net.blay09.mods.refinedrelocation.container.NameFilterContainer;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class NameFilterScreen extends FilterScreen<NameFilterContainer> implements IFilterPreviewGui, IHasContainer<NameFilterContainer> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/root_filter.png");

    private static final int UPDATE_INTERVAL = 20;

    private MultiLineTextFieldWidget txtFilter;

    private int ticksSinceUpdate;
    private String lastSentText = "";

    public NameFilterScreen(NameFilterContainer container, PlayerInventory playerInventory, ITextComponent displayName) {
        super(container, playerInventory, displayName);

        ySize = 210;
        shouldKeyRepeat = true;
    }

    @Override
    public void init() {
        super.init();

        txtFilter = new MultiLineTextFieldWidget(guiLeft + 8, guiTop + 20, 150, 84);
        txtFilter.changeFocus(true);
        txtFilter.setCanLoseFocus(false);
        txtFilter.setText(container.getValue());
        addButton(txtFilter);
        setFocusedDefault(txtFilter);

        GuiScrollBar scrollBar = new GuiScrollBar(guiLeft + 161, guiTop + 20, 83, txtFilter);
        addButton(scrollBar);

        GuiScrollPane scrollPane = new GuiScrollPane(scrollBar, guiLeft + 8, guiTop + 20, 150, 84);
        addButton(scrollPane);
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
        getMinecraft().getTextureManager().bindTexture(TEXTURE);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        getMinecraft().fontRenderer.drawString(I18n.format("container.refinedrelocation:name_filter"), 8, 6, 4210752);
        getMinecraft().fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    public boolean onGuiAboutToClose() {
        super.onGuiAboutToClose();
        container.sendValueToServer(txtFilter.getText());
        RefinedRelocationAPI.returnToParentContainer();
        return false;
    }

    @Override
    public Container getFilterContainer() {
        return container;
    }

    @Override
    public int getFilterGuiLeft() {
        return guiLeft;
    }

    @Override
    public int getFilterGuiTop() {
        return guiTop;
    }
}
