package net.blay09.mods.refinedrelocation.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.client.IFilterPreviewGui;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollBar;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollPane;
import net.blay09.mods.refinedrelocation.client.gui.base.element.IScrollTarget;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiChecklistEntry;
import net.blay09.mods.refinedrelocation.container.ChecklistFilterContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ChecklistFilterScreen extends FilterScreen<ChecklistFilterContainer> implements IScrollTarget, IFilterPreviewGui, IHasContainer<ChecklistFilterContainer> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/checklist_filter.png");

    private final IChecklistFilter filter;
    private final GuiChecklistEntry[] entries = new GuiChecklistEntry[7];

    private int currentOffset;

    public ChecklistFilterScreen(ChecklistFilterContainer container, PlayerInventory playerInventory, ITextComponent displayName) {
        super(container, playerInventory, displayName);
        this.filter = container.getFilter();
        ySize = 210;
        titleX = 8;
        titleY = 6;
        playerInventoryTitleX = 8;
        playerInventoryTitleY = ySize - 96 + 2;
    }

    @Override
    public void init() {
        super.init();

        GuiScrollBar scrollBar = new GuiScrollBar(guiLeft + xSize - 16, guiTop + 28, 75, this);
        addButton(scrollBar);

        GuiScrollPane scrollPane = new GuiScrollPane(scrollBar, guiLeft + 8, guiTop + 28, 152, 80);
        children.add(scrollPane);

        int y = guiTop + 28;
        for (int i = 0; i < entries.length; i++) {
            entries[i] = new GuiChecklistEntry(guiLeft + 8, y, filter);
            addButton(entries[i]);
            y += entries[i].getHeight();
        }

        setCurrentOffset(0);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public int getVisibleRows() {
        return entries.length;
    }

    @Override
    public int getRowCount() {
        return filter.getOptionCount();
    }

    @Override
    public int getCurrentOffset() {
        return currentOffset;
    }

    @Override
    public void setCurrentOffset(int offset) {
        this.currentOffset = offset;
        for (int i = 0; i < entries.length; i++) {
            int optionIndex = currentOffset + i;
            if (optionIndex >= filter.getOptionCount()) {
                optionIndex = -1;
            }
            entries[i].setCurrentOption(optionIndex);
        }
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
