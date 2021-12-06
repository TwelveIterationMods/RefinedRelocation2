package net.blay09.mods.refinedrelocation.client.gui.base.element;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TextComponent;

public class ScrollPaneWidget extends AbstractWidget {

    private final ScrollBarWidget scrollBar;

    public ScrollPaneWidget(ScrollBarWidget scrollBar, int x, int y, int width, int height) {
        super(x, y, width, height, new TextComponent(""));
        this.scrollBar = scrollBar;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (isMouseOver(mouseX, mouseY)) {
            scrollBar.forceMouseScrolled(delta);
            return true;
        }

        return false;
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
    }


    @Override
    public void updateNarration(NarrationElementOutput output) {
    }
}
