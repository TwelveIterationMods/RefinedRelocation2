package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.client.IDrawable;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.client.gui.AddFilterScreen;
import net.blay09.mods.refinedrelocation.client.gui.RootFilterScreen;
import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.blay09.mods.refinedrelocation.client.gui.base.ITooltipElement;
import net.blay09.mods.refinedrelocation.container.ContainerRootFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class GuiFilterSlot extends Button implements ITooltipElement {

    private final RootFilterScreen parentGui;
    private final IDrawable texture;
    private final IRootFilter rootFilter;
    private final int index;

    public GuiFilterSlot(int x, int y, RootFilterScreen parentGui, IRootFilter rootFilter, int index) {
        super(x, y, 24, 24, "", it -> {
        });
        this.parentGui = parentGui;
        this.rootFilter = rootFilter;
        this.index = index;
        texture = GuiTextures.FILTER_SLOT;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        texture.bind();
        texture.draw(x, y, blitOffset);

        IFilter filter = rootFilter.getFilter(index);
        if (filter != null) {
            IDrawable filterIcon = filter.getFilterIcon();
            if (filterIcon != null) {
                filterIcon.draw(x, y, 24, 24, blitOffset);
            }
        }
        if (isMouseOver(mouseX, mouseY)) {
            fill(x + 1, y + 1, x + width - 1, y + height - 1, 0x99FFFFFF);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        IFilter filter = rootFilter.getFilter(index);
        if (filter == null) {
            Minecraft.getInstance().displayGuiScreen(new AddFilterScreen(parentGui, parentGui.getPlayerInventory(), parentGui.getTitle()));
        } else {
            RefinedRelocationAPI.sendContainerMessageToServer(ContainerRootFilter.KEY_EDIT_FILTER, index);
        }
    }

    @Override
    public void addTooltip(List<String> list) {
        IFilter filter = rootFilter.getFilter(index);
        if (filter == null) {
            list.add(TextFormatting.GRAY + I18n.format("gui.refinedrelocation:root_filter.no_filter_set"));
            list.add(TextFormatting.YELLOW + I18n.format("gui.refinedrelocation:root_filter.click_to_add_filter"));
        } else {
            list.add(I18n.format(filter.getLangKey()));
            if (filter.hasConfiguration()) {
                list.add(TextFormatting.YELLOW + I18n.format("gui.refinedrelocation:root_filter.click_to_configure"));
            } else {
                list.add(TextFormatting.GRAY + I18n.format("gui.refinedrelocation:root_filter.not_configurable"));
            }
        }
    }

    public int getFilterIndex() {
        return index;
    }

    public boolean hasFilter() {
        return rootFilter.getFilter(index) != null;
    }
}
