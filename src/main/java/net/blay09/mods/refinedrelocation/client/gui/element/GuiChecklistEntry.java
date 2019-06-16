package net.blay09.mods.refinedrelocation.client.gui.element;

import com.mojang.blaze3d.platform.GlStateManager;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.client.IDrawable;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.blay09.mods.refinedrelocation.container.ChecklistFilterContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;

public class GuiChecklistEntry extends Button {

    private final IChecklistFilter filter;
    private final IDrawable texture;
    private final IDrawable textureChecked;

    private int currentOption = -1;

    public GuiChecklistEntry(int x, int y, IChecklistFilter filter) {
        super(x, y, 151, 11, "", it -> {
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
                RefinedRelocationAPI.sendContainerMessageToServer(ChecklistFilterContainer.KEY_CHECK, currentOption);
            } else {
                RefinedRelocationAPI.sendContainerMessageToServer(ChecklistFilterContainer.KEY_UNCHECK, currentOption);
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (isMouseOver(mouseX, mouseY)) {
            fill(x, y, x + width, y + height, 0x66FFFFFF);
        }

        GlStateManager.color4f(1f, 1f, 1f, 1f);

        if (currentOption != -1) {
            texture.bind();
            if (filter.isOptionChecked(currentOption)) {
                textureChecked.draw(x + 1, y + height / 2f - 11 / 2f, blitOffset);
            } else {
                texture.draw(x + 1, y + height / 2f - 11 / 2f, blitOffset);
            }

            FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
            drawString(fontRenderer, I18n.format(filter.getOptionLangKey(currentOption)), x + 14, y + height / 2 - fontRenderer.FONT_HEIGHT / 2, 0xFFFFFF);
        }
    }

    public void setCurrentOption(int currentOption) {
        this.currentOption = currentOption;
    }

}
