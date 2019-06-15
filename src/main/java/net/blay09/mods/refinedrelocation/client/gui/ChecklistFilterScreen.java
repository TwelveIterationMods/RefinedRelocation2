package net.blay09.mods.refinedrelocation.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.client.IFilterPreviewGui;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.client.gui.base.GuiContainerMod;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollBar;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollPane;
import net.blay09.mods.refinedrelocation.client.gui.base.element.IScrollTarget;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiChecklistEntry;
import net.blay09.mods.refinedrelocation.container.ContainerBlockExtender;
import net.blay09.mods.refinedrelocation.container.ContainerChecklistFilter;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ChecklistFilterScreen extends GuiContainerMod<ContainerChecklistFilter> implements IScrollTarget, IFilterPreviewGui {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/checklist_filter.png");

    private final IChecklistFilter filter;
    private final GuiChecklistEntry[] entries = new GuiChecklistEntry[7];

    private int currentOffset;

    public ChecklistFilterScreen(ContainerChecklistFilter container, PlayerInventory playerInventory, ITextComponent displayName, TileEntity tileEntity, IChecklistFilter filter) {
        super(container, playerInventory, displayName);
        this.filter = filter;
        ySize = 210;
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
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        minecraft.getTextureManager().bindTexture(TEXTURE);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        minecraft.fontRenderer.drawString(I18n.format(filter.getLangKey()), 8, 6, 4210752);
        minecraft.fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
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
    public int getLeft() {
        return guiLeft;
    }

    @Override
    public int getTop() {
        return guiTop;
    }
}
