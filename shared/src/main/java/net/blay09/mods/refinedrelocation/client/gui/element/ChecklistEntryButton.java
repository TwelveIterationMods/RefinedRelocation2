package net.blay09.mods.refinedrelocation.client.gui.element;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.client.IDrawable;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.blay09.mods.refinedrelocation.menu.ChecklistFilterMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

public class ChecklistEntryButton extends Button {

    private final IChecklistFilter filter;
    private final IDrawable texture;
    private final IDrawable textureChecked;

    private int currentOption = -1;

    public ChecklistEntryButton(int x, int y, IChecklistFilter filter) {
        super(x, y, 151, 11, Component.empty(), it -> {
        });
        this.filter = filter;
        texture = GuiTextures.CHECKLIST;
        textureChecked = GuiTextures.CHECKLIST_CHECKED;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (currentOption != -1) {
            boolean oldState = filter.isOptionChecked(currentOption);
            filter.setOptionChecked(currentOption, !oldState);
            if (!oldState) {
                RefinedRelocationAPI.sendContainerMessageToServer(ChecklistFilterMenu.KEY_CHECK, currentOption);
            } else {
                RefinedRelocationAPI.sendContainerMessageToServer(ChecklistFilterMenu.KEY_UNCHECK, currentOption);
            }
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (isMouseOver(mouseX, mouseY)) {
            fill(poseStack, x, y, x + width, y + height, 0x66FFFFFF);
        }

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        if (currentOption != -1) {
            texture.bind();
            if (filter.isOptionChecked(currentOption)) {
                textureChecked.draw(poseStack, x + 1, y + height / 2f - 11 / 2f, getBlitOffset());
            } else {
                texture.draw(poseStack, x + 1, y + height / 2f - 11 / 2f, getBlitOffset());
            }

            Font font = Minecraft.getInstance().font;
            drawString(poseStack, font, I18n.get(filter.getOptionLangKey(currentOption)), x + 14, y + height / 2 - font.lineHeight / 2, 0xFFFFFF);
        }
    }

    public void setCurrentOption(int currentOption) {
        this.currentOption = currentOption;
    }

}
