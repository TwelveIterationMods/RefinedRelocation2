package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.blay09.mods.refinedrelocation.client.gui.RootFilterScreen;
import net.blay09.mods.refinedrelocation.client.gui.base.ITickableElement;
import net.blay09.mods.refinedrelocation.client.gui.base.ITooltipElement;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiImageButton;
import net.blay09.mods.refinedrelocation.container.RootFilterContainer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

import static net.blay09.mods.refinedrelocation.util.TextUtils.formattedTranslation;

public class GuiWhitelistButton extends GuiImageButton implements ITickableElement, ITooltipElement {

    private final RootFilterScreen parentGui;
    private final GuiFilterSlot parentSlot;

    private boolean lastBlacklist;

    public GuiWhitelistButton(int x, int y, RootFilterScreen parentGui, GuiFilterSlot parentSlot) {
        super(x, y, 8, 8, GuiTextures.FILTER_WHITELIST, it -> {
        });
        this.parentGui = parentGui;
        this.parentSlot = parentSlot;
        visible = false;
    }

    @Override
    public void tick() {
        boolean nowBlacklist = parentGui.getContainer().getRootFilter().isBlacklist(parentSlot.getFilterIndex());
        if (lastBlacklist != nowBlacklist) {
            setTexture(nowBlacklist ? GuiTextures.FILTER_BLACKLIST : GuiTextures.FILTER_WHITELIST);
            lastBlacklist = nowBlacklist;
        }

        visible = parentSlot.hasFilter();
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        boolean isBlacklist = !parentGui.getContainer().getRootFilter().isBlacklist(parentSlot.getFilterIndex());
        CompoundNBT tagCompound = new CompoundNBT();
        tagCompound.putInt(RootFilterContainer.KEY_BLACKLIST_INDEX, parentSlot.getFilterIndex());
        tagCompound.putBoolean(RootFilterContainer.KEY_BLACKLIST, isBlacklist);
        RefinedRelocationAPI.sendContainerMessageToServer(RootFilterContainer.KEY_BLACKLIST, tagCompound);
        parentGui.getContainer().getRootFilter().setIsBlacklist(parentSlot.getFilterIndex(), isBlacklist);
    }

    @Override
    public void addTooltip(List<ITextComponent> list) {
        boolean nowBlacklist = parentGui.getContainer().getRootFilter().isBlacklist(parentSlot.getFilterIndex());
        list.add(formattedTranslation(TextFormatting.WHITE, nowBlacklist ? "gui.refinedrelocation:root_filter.blacklist" : "gui.refinedrelocation:root_filter.whitelist"));
        list.add(formattedTranslation(TextFormatting.YELLOW, "gui.refinedrelocation:root_filter.click_to_toggle"));
    }

}
