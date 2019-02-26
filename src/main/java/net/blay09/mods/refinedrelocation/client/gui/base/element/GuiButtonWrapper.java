package net.blay09.mods.refinedrelocation.client.gui.base.element;

import net.blay09.mods.refinedrelocation.client.gui.base.IParentScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.util.function.BiConsumer;

public class GuiButtonWrapper extends GuiElement {

    protected final GuiButton button;

    public GuiButtonWrapper(int buttonId, int x, int y, int width, int height, String text, BiConsumer<Double, Double> clickHandler) {
        button = new GuiButton(buttonId, x, y, width, height, text) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                super.onClick(mouseX, mouseY);
                clickHandler.accept(mouseX, mouseY);
            }
        };
    }

    public GuiButtonWrapper(GuiButton button) {
        this.button = button;
        setPosition(button.x, button.y);
        setSize(button.width, button.height);
    }

    @Override
    public final boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        Minecraft mc = Minecraft.getInstance();
        if (button.mouseClicked(mouseX, mouseY, mouseButton)) {
            button.playPressSound(mc.getSoundHandler());
            actionPerformed();
            return true;
        }
        return false;
    }

    @Override
    public void drawBackground(IParentScreen parentScreen, int mouseX, int mouseY, float partialTicks) {
        super.drawBackground(parentScreen, mouseX, mouseY, partialTicks);
        button.x = getAbsoluteX();
        button.y = getAbsoluteY();
        button.width = getWidth();
        button.height = getHeight();
        button.render(mouseX, mouseY, partialTicks);
    }

    public void actionPerformed() {
    }

}
