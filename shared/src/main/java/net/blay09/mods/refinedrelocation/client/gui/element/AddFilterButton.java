package net.blay09.mods.refinedrelocation.client.gui.element;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.client.IDrawable;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.blay09.mods.refinedrelocation.client.gui.base.ITooltipElement;
import net.blay09.mods.refinedrelocation.client.gui.base.element.ImageButton;
import net.blay09.mods.refinedrelocation.menu.AddFilterMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.List;

public class AddFilterButton extends ImageButton implements ITooltipElement {

    private IFilter currentFilter;

    public AddFilterButton(int x, int y) {
        super(x, y, 151, 27, GuiTextures.BUTTON_NONE, it -> {
        });
    }

    @Override
    public void onClick(double p_194829_1_, double p_194829_3_) {
        if (currentFilter != null) {
            RefinedRelocationAPI.sendContainerMessageToServer(AddFilterMenu.KEY_ADD_FILTER, currentFilter.getIdentifier());
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.render(poseStack, mouseX, mouseY, partialTicks);

        if (isHovered) {
            fill(poseStack, x, y, x + width, y + height, 0xAAFFFFFF);
        }

        if (currentFilter != null) {
            IDrawable filterIcon = currentFilter.getFilterIcon();
            if (filterIcon != null) {
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                filterIcon.draw(poseStack, x + 2, y + height / 2f - 12, 24, 24, getBlitOffset());
            }

            Font font = Minecraft.getInstance().font;
            drawString(poseStack, font, I18n.get(currentFilter.getLangKey()), x + 32, y + height / 2 - font.lineHeight / 2, 0xFFFFFF);
        }
    }

    public void setCurrentFilter(@Nullable IFilter currentFilter) {
        this.currentFilter = currentFilter;
    }

    @Override
    public void addTooltip(List<Component> tooltip) {
        if (currentFilter != null) {
            for (String text : I18n.get(currentFilter.getDescriptionLangKey()).split("\\\\n")) {
                tooltip.add(Component.literal(text));
            }
        }
    }

}
