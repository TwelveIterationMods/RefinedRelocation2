package net.blay09.mods.refinedrelocation.client.gui.base;

import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiLabel;
import net.blay09.mods.refinedrelocation.client.gui.base.element.GuiTextFieldMultiLine;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class GuiContainerMod<T extends Container> extends ContainerScreen<T> {

    protected final T container;
    protected boolean shouldKeyRepeat;

    public GuiContainerMod(T container, PlayerInventory playerInventory, ITextComponent displayName) {
        super(container, playerInventory, displayName);
        this.container = container;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void init() {
        super.init();

        if (shouldKeyRepeat) {
            minecraft.keyboardListener.enableRepeatEvents(true);
        }
    }

    public boolean onGuiAboutToClose() {
        return true;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void onClose() {
        super.onClose();

        if (shouldKeyRepeat) {
            minecraft.keyboardListener.enableRepeatEvents(false);
        }
    }

    @Override
    public void tick() {
        super.tick();

        for (IGuiEventListener child : children) {
            if (child instanceof GuiTextFieldMultiLine) {
                ((GuiTextFieldMultiLine) child).tick();
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        for (IGuiEventListener child : children) {
            if (child instanceof GuiLabel) {
                ((GuiLabel) child).render(mouseX, mouseY, partialTicks);
            } else if (child instanceof GuiTextFieldMultiLine) {
                ((GuiTextFieldMultiLine) child).drawTextField(mouseX, mouseY, partialTicks);
            }
        }

        renderHoveredToolTip(mouseX, mouseY);
    }

    public T getContainer() {
        return container;
    }
}
