package net.blay09.mods.refinedrelocation.client.gui.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class GuiContainerMod<T extends Container> extends GuiContainer implements IParentScreen {

    protected final T container;
    protected boolean shouldKeyRepeat;

    public GuiContainerMod(T container) {
        super(container);
        this.container = container;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void initGui() {
        super.initGui();

        if (shouldKeyRepeat) {
            mc.keyboardListener.enableRepeatEvents(true);
        }
    }

    public boolean onGuiAboutToClose() {
        return true;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void onGuiClosed() {
        super.onGuiClosed();

        if (shouldKeyRepeat) {
            mc.keyboardListener.enableRepeatEvents(false);
        }
    }

    @Override
    public void tick() {
        super.tick();

    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    public T getContainer() {
        return container;
    }

    @Override
    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    @Override
    public Minecraft getMinecraft() {
        return mc;
    }

    @Override
    public int getLeft() {
        return guiLeft;
    }

    @Override
    public int getTop() {
        return guiTop;
    }

    @Override
    public int getWidth() {
        return xSize;
    }

    @Override
    public int getHeight() {
        return ySize;
    }

}
