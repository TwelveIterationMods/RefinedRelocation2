package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.blay09.mods.refinedrelocation.client.gui.base.ITickableElement;
import net.blay09.mods.refinedrelocation.client.gui.base.ITooltipElement;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiImageButton;
import net.blay09.mods.refinedrelocation.container.RootFilterContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

import static net.blay09.mods.refinedrelocation.util.TextUtils.formattedTranslation;

public class GuiDeleteFilterButton extends GuiImageButton implements ITickableElement, ITooltipElement {

    private static boolean shiftGuard;

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

        boolean isShiftDown = Screen.hasShiftDown();
        active = !shiftGuard && isShiftDown;
        if (!isShiftDown) {
            shiftGuard = false;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY) && !active) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.visible && mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        shiftGuard = true;
        RefinedRelocationAPI.sendContainerMessageToServer(RootFilterContainer.KEY_DELETE_FILTER, parentSlot.getFilterIndex());
    }

    @Override
    public void addTooltip(List<ITextProperties> list) {
        list.add(formattedTranslation(TextFormatting.RED, "gui.refinedrelocation:root_filter.delete_filter"));
        if (!active) {
            list.add(formattedTranslation(TextFormatting.YELLOW, "gui.refinedrelocation:root_filter.hold_shift_to_delete"));
        } else {
            list.add(formattedTranslation(TextFormatting.YELLOW, "gui.refinedrelocation:root_filter.delete_cannot_be_undone"));
        }
    }

}
