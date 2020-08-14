package net.blay09.mods.refinedrelocation.client.gui.element;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.client.IDrawable;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.blay09.mods.refinedrelocation.client.gui.base.ITooltipElement;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiImageButton;
import net.blay09.mods.refinedrelocation.container.AddFilterContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.List;

public class GuiAddFilterButton extends GuiImageButton implements ITooltipElement {

    private IFilter currentFilter;

    public GuiAddFilterButton(int x, int y) {
        super(x, y, 151, 27, GuiTextures.BUTTON_NONE, it -> {
        });
    }

    @Override
    public void onClick(double p_194829_1_, double p_194829_3_) {
        if (currentFilter != null) {
            RefinedRelocationAPI.sendContainerMessageToServer(AddFilterContainer.KEY_ADD_FILTER, currentFilter.getIdentifier());
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        if (isHovered) {
            fill(matrixStack, x, y, x + width, y + height, 0xAAFFFFFF);
        }

        if (currentFilter != null) {
            IDrawable filterIcon = currentFilter.getFilterIcon();
            if (filterIcon != null) {
                RenderSystem.color4f(1f, 1f, 1f, 1f);
                filterIcon.draw(matrixStack, x + 2, y + height / 2f - 12, 24, 24, getBlitOffset());
            }

            FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
            drawString(matrixStack, fontRenderer, I18n.format(currentFilter.getLangKey()), x + 32, y + height / 2 - fontRenderer.FONT_HEIGHT / 2, 0xFFFFFF);
        }
    }

    public void setCurrentFilter(@Nullable IFilter currentFilter) {
        this.currentFilter = currentFilter;
    }

    @Override
    public void addTooltip(List<ITextComponent> tooltip) {
        if (currentFilter != null) {
            for (String text : I18n.format(currentFilter.getDescriptionLangKey()).split("\\\\n")) {
                tooltip.add(new StringTextComponent(text));
            }
        }
    }

}
