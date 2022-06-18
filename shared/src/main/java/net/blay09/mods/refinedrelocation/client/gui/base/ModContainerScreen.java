package net.blay09.mods.refinedrelocation.client.gui.base;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.balm.mixin.ScreenAccessor;
import net.blay09.mods.refinedrelocation.client.gui.base.element.MultiLineTextFieldWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.lwjgl.glfw.GLFW;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.ArrayList;
import java.util.List;

public abstract class ModContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    protected boolean shouldKeyRepeat;

    public ModContainerScreen(T menu, Inventory inventory, Component displayName) {
        super(menu, inventory, displayName);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void init() {
        super.init();

        if (shouldKeyRepeat) {
            minecraft.keyboardHandler.setSendRepeatsToGui(true);
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
            minecraft.keyboardHandler.setSendRepeatsToGui(false);
        }
    }

    @Override
    public void containerTick() {
        super.containerTick();

        for (GuiEventListener child : saneChildren) {
            if (child instanceof MultiLineTextFieldWidget) {
                ((MultiLineTextFieldWidget) child).tick();
            } else if (child instanceof ITickableElement) {
                ((ITickableElement) child).tick();
            }
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        renderTooltip(poseStack, mouseX, mouseY);

        for (int i = saneChildren.size() - 1; i >= 0; i--) {
            GuiEventListener child = saneChildren.get(i);
            if (child instanceof ITooltipElement && child.isMouseOver(mouseX, mouseY)) {
                List<Component> tooltip = new ArrayList<>();
                ((ITooltipElement) child).addTooltip(tooltip);
                renderComponentTooltip(poseStack, tooltip, mouseX, mouseY);
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
    private List<GuiEventListener> saneChildren = new ArrayList<>();

    @Override
    protected <V extends GuiEventListener & NarratableEntry> V addWidget(V widget) {
        ((ScreenAccessor) this).getChildren().add(widget);
        ((ScreenAccessor) this).getNarratables().add(widget);
        this.saneChildren.add(widget);
        return widget;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifier) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            Minecraft.getInstance().player.closeContainer();
        }

        GuiEventListener focused = this.getFocused();
        if (focused instanceof EditBox) {
            if (focused.keyPressed(keyCode, scanCode, modifier) || ((EditBox) focused).canConsumeInput()) {
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifier);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (GuiEventListener listener : saneChildren) {
            listener.mouseReleased(mouseX, mouseY, button);
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (int i = saneChildren.size() - 1; i >= 0; i--) {
            GuiEventListener listener = saneChildren.get(i);
            if (listener.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double button) {
        for (int i = saneChildren.size() - 1; i >= 0; i--) {
            GuiEventListener listener = saneChildren.get(i);
            if (listener.mouseScrolled(mouseX, mouseY, button)) {
                return true;
            }
        }

        return super.mouseScrolled(mouseX, mouseY, button);
    }

    public boolean isTopMostElement(Widget widget, double mouseX, double mouseY) {
        for (int i = saneChildren.size() - 1; i >= 0; i--) {
            GuiEventListener listener = saneChildren.get(i);
            if (listener.isMouseOver(mouseX, mouseY)) {
                return listener == widget;
            }
        }

        return false;
    }
}
