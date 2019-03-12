package net.blay09.mods.refinedrelocation.client.gui;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.client.IFilterPreviewGui;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.client.gui.base.GuiContainerMod;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollBar;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollPane;
import net.blay09.mods.refinedrelocation.client.gui.base.element.IScrollTarget;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiChecklistEntry;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiReturnFromFilterButton;
import net.blay09.mods.refinedrelocation.container.ContainerChecklistFilter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiChecklistFilter extends GuiContainerMod<ContainerChecklistFilter> implements IScrollTarget, IFilterPreviewGui {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/checklist_filter.png");

    private final IChecklistFilter filter;
    private final GuiChecklistEntry[] entries = new GuiChecklistEntry[7];

    private int currentOffset;

    public GuiChecklistFilter(EntityPlayer player, TileEntity tileEntity, IChecklistFilter filter) {
        super(new ContainerChecklistFilter(player, tileEntity, filter));
        this.filter = filter;
        ySize = 210;
    }

    @Override
    public void initGui() {
        super.initGui();

        GuiScrollBar scrollBar = new GuiScrollBar(0, guiLeft + xSize - 16, guiTop + 28, 75, this);
        addButton(scrollBar);

        GuiScrollPane scrollPane = new GuiScrollPane(scrollBar, guiLeft + 8, guiTop + 28, 152, 80);
        children.add(scrollPane);

        int y = guiTop + 28;
        for (int i = 0; i < entries.length; i++) {
            entries[i] = new GuiChecklistEntry(0, guiLeft + 8, y, filter);
            addButton(entries[i]);
            y += entries[i].height;
        }

        addButton(new GuiReturnFromFilterButton(0, guiLeft + xSize - 20, guiTop + 4));

        setCurrentOffset(0);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        fontRenderer.drawString(I18n.format(filter.getLangKey()), 8, 6, 4210752);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
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
