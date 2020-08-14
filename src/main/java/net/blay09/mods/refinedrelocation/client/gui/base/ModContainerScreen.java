package net.blay09.mods.refinedrelocation.client.gui.base;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.refinedrelocation.client.gui.base.element.MultiLineTextFieldWidget;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import org.lwjgl.glfw.GLFW;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.ArrayList;
import java.util.List;

public abstract class ModContainerScreen<T extends Container> extends ContainerScreen<T> {

    protected final T container;
    protected boolean shouldKeyRepeat;

    public ModContainerScreen(T container, PlayerInventory playerInventory, ITextComponent displayName) {
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

        for (IGuiEventListener child : saneChildren) {
            if (child instanceof MultiLineTextFieldWidget) {
                ((MultiLineTextFieldWidget) child).tick();
            } else if (child instanceof ITickableElement) {
                ((ITickableElement) child).tick();
            }
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        func_230459_a_(matrixStack, mouseX, mouseY); // renderHoveredTooltip

        for (int i = saneChildren.size() - 1; i >= 0; i--) {
            IGuiEventListener child = saneChildren.get(i);
            if (child instanceof ITooltipElement && child.isMouseOver(mouseX, mouseY)) {
                List<ITextComponent> tooltip = new ArrayList<>();
                ((ITooltipElement) child).addTooltip(tooltip);
                func_243308_b(matrixStack, tooltip, mouseX, mouseY);
                break;
            }
        }
    }

    /**
     * We can't use Mojang's event system as it's completely wrong, e.g.
     * - mouseClicked is handled in direction of rendering, meaning you always click things rendered below others
     * - mouseReleased is only handled for the top element under mouse, meaning if you let go of a scrollbar outside of
     * it won't recognize the mouse as let go
     * Simply overriding the event handling isn't good enough as we still have to delegate to super for ContainerScreen
     * input handling, so there's no choice other than to handle our own sane list separately with a sane
     * implementation.
     */
    private List<IGuiEventListener> saneChildren = new ArrayList<>();

    @Override
    protected <V extends Widget> V addButton(V button) {
        this.buttons.add(button);
        this.saneChildren.add(button);
        return button;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifier) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            getMinecraft().player.closeScreen();
        }

        IGuiEventListener focused = this.getListener();
        if (focused instanceof TextFieldWidget) {
            if (focused.keyPressed(keyCode, scanCode, modifier) || ((TextFieldWidget) focused).canWrite()) {
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifier);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (IGuiEventListener listener : saneChildren) {
            listener.mouseReleased(mouseX, mouseY, button);
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (int i = saneChildren.size() - 1; i >= 0; i--) {
            IGuiEventListener listener = saneChildren.get(i);
            if (listener.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double button) {
        for (int i = saneChildren.size() - 1; i >= 0; i--) {
            IGuiEventListener listener = saneChildren.get(i);
            if (listener.mouseScrolled(mouseX, mouseY, button)) {
                return true;
            }
        }

        return super.mouseScrolled(mouseX, mouseY, button);
    }

    public boolean isTopMostElement(Widget widget, double mouseX, double mouseY) {
        for (int i = saneChildren.size() - 1; i >= 0; i--) {
            IGuiEventListener listener = saneChildren.get(i);
            if (listener.isMouseOver(mouseX, mouseY)) {
                return listener == widget;
            }
        }

        return false;
    }
}
