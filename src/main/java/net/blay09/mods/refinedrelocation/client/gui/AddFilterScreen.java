package net.blay09.mods.refinedrelocation.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.client.gui.base.ModContainerScreen;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollBar;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollPane;
import net.blay09.mods.refinedrelocation.client.gui.base.element.IScrollTarget;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiAddFilterButton;
import net.blay09.mods.refinedrelocation.container.AddFilterContainer;
import net.blay09.mods.refinedrelocation.filter.FilterRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class AddFilterScreen extends ModContainerScreen<AddFilterContainer> implements IScrollTarget {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/add_filter.png");

    private final GuiAddFilterButton[] filterButtons = new GuiAddFilterButton[3];
    private final List<IFilter> filterList;

    private int currentOffset;

    public AddFilterScreen(AddFilterContainer container, PlayerInventory playerInventory, ITextComponent displayName) {
        super(container, playerInventory, displayName);
        filterList = FilterRegistry.getApplicableFilters(t -> t.isFilterUsable(container.getTileEntity()));
        ySize = 210;
    }

    @Override
    public void init() {
        super.init();

        GuiScrollBar scrollBar = new GuiScrollBar(guiLeft + xSize - 16, guiTop + 28, 78, this);
        addButton(scrollBar);

        GuiScrollPane scrollPane = new GuiScrollPane(scrollBar, guiLeft + 8, guiTop + 28, 152, 80);
        children.add(scrollPane);

        int y = guiTop + 28;
        for (int i = 0; i < filterButtons.length; i++) {
            filterButtons[i] = new GuiAddFilterButton(guiLeft + 8, y);
            addButton(filterButtons[i]);
            y += filterButtons[i].getHeight();
        }

        setCurrentOffset(0);
    }

    @Override // drawGuiContainerBackgroundLayer
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void func_230451_b_(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
    }

    @Override
    public int getVisibleRows() {
        return filterButtons.length;
    }

    @Override
    public int getRowCount() {
        return filterList.size();
    }

    @Override
    public int getCurrentOffset() {
        return currentOffset;
    }

    @Override
    public void setCurrentOffset(int offset) {
        this.currentOffset = offset;

        for (int i = 0; i < filterButtons.length; i++) {
            int filterIndex = currentOffset + i;
            IFilter filter = null;
            if (filterIndex >= 0 && filterIndex < filterList.size()) {
                filter = filterList.get(filterIndex);
            }
            filterButtons[i].setCurrentFilter(filter);
        }
    }

}
