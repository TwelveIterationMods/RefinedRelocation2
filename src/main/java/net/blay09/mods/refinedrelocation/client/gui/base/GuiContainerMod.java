package net.blay09.mods.refinedrelocation.client.gui.base;

import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiLabel;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class GuiContainerMod<T extends Container> extends GuiContainer {

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
        for (IGuiEventListener child : children) {
            if (child instanceof GuiLabel) {
                ((GuiLabel) child).render(mouseX, mouseY, partialTicks);
            }
        }

        renderHoveredToolTip(mouseX, mouseY);
    }

    public T getContainer() {
        return container;
    }
}
