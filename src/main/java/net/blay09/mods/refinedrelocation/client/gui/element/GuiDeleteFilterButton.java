package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.blay09.mods.refinedrelocation.client.gui.base.ITickableElement;
import net.blay09.mods.refinedrelocation.client.gui.base.ITooltipElement;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiImageButton;
import net.blay09.mods.refinedrelocation.container.RootFilterContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class GuiDeleteFilterButton extends GuiImageButton implements ITickableElement, ITooltipElement {

    private final GuiFilterSlot parentSlot;

    public GuiDeleteFilterButton(int x, int y, GuiFilterSlot parentSlot) {
        super(x, y, 8, 8, GuiTextures.DELETE_FILTER, it -> {
        });
        this.parentSlot = parentSlot;
        visible = false;
    }

    @Override
    public void tick() {
        visible = parentSlot.hasFilter();
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        RefinedRelocationAPI.sendContainerMessageToServer(RootFilterContainer.KEY_DELETE_FILTER, parentSlot.getFilterIndex());
    }

    @Override
    public void addTooltip(List<String> list) {
        list.add(TextFormatting.RED + I18n.format("gui.refinedrelocation:root_filter.delete_filter"));
    }

}
