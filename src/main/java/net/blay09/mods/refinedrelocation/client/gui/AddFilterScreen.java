package net.blay09.mods.refinedrelocation.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.client.gui.base.ModContainerScreen;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollBar;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiScrollPane;
import net.blay09.mods.refinedrelocation.client.gui.base.element.IScrollTarget;
import net.blay09.mods.refinedrelocation.client.gui.element.GuiAddFilterButton;
import net.blay09.mods.refinedrelocation.container.RootFilterContainer;
import net.blay09.mods.refinedrelocation.filter.FilterRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class AddFilterScreen extends ModContainerScreen<RootFilterContainer> implements IScrollTarget {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RefinedRelocation.MOD_ID, "textures/gui/add_filter.png");

    private final RootFilterScreen parentGui;
    private final GuiAddFilterButton[] filterButtons = new GuiAddFilterButton[3];
    private final List<IFilter> filterList;

    private int currentOffset;

    public AddFilterScreen(RootFilterScreen parentGui, PlayerInventory playerInventory, ITextComponent displayName) {
        super(parentGui.getContainer(), playerInventory, displayName);
        this.parentGui = parentGui;
        filterList = FilterRegistry.getApplicableFilters(t -> t.isFilterUsable(parentGui.getContainer().getTileEntity()));
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
            filterButtons[i] = new GuiAddFilterButton(guiLeft + 8, y, this);
            addButton(filterButtons[i]);
            y += filterButtons[i].getHeight();
        }

        setCurrentOffset(0);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            minecraft.displayGuiScreen(parentGui);
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
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

        minecraft.fontRenderer.drawString(I18n.format("container.refinedrelocation:add_filter"), 8, 6, 4210752);
        minecraft.fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
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

    public RootFilterScreen getParentGui() {
        return parentGui;
    }

}
