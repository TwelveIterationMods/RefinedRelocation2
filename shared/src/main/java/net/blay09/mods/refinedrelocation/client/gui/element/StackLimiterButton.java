package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.client.gui.base.ITickableElement;
import net.blay09.mods.refinedrelocation.client.gui.base.ITooltipElement;
import net.blay09.mods.refinedrelocation.client.gui.base.element.SizableButton;
import net.blay09.mods.refinedrelocation.block.entity.BlockExtenderBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

import static net.blay09.mods.refinedrelocation.util.TextUtils.formattedTranslation;

public class StackLimiterButton extends SizableButton implements ITickableElement, ITooltipElement {

    private final BlockExtenderBlockEntity blockExtender;

    public StackLimiterButton(int x, int y, int width, int height, BlockExtenderBlockEntity blockExtender) {
        super(x, y, width, height, new TextComponent(""), it -> {
        });
        this.blockExtender = blockExtender;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (isMouseOver(mouseX, mouseY)) {
            playDownSound(Minecraft.getInstance().getSoundManager());
            onClick(mouseX, mouseY, mouseButton);
        }

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void onClick(double mouseX, double mouseY, int mouseButton) {
        int limit = blockExtender.getStackLimiterLimit();
        int index = (int) (Math.log(limit) / Math.log(2));
        int maxStackSize = Items.AIR.getItemStackLimit(ItemStack.EMPTY);
        int maxIndex = (int) (Math.log(maxStackSize) / Math.log(2));
        if (mouseButton == 0) {
            if (index < maxIndex) {
                index++;
            }
        } else if (mouseButton == 1) {
            if (index > 0) {
                index--;
            }
        }

        blockExtender.setStackLimiterLimit((int) Math.pow(2, index));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int limit = blockExtender.getStackLimiterLimit();
        if (delta > 0) {
            limit++;
        } else if (delta < 0) {
            limit--;
        }

        limit = Mth.clamp(limit, 1, Items.AIR.getItemStackLimit(ItemStack.EMPTY));
        blockExtender.setStackLimiterLimit(limit);
        return true;
    }

    @Override
    public void tick() {
        setMessage(new TextComponent(String.valueOf(blockExtender.getStackLimiterLimit())));
    }

    @Override
    public void addTooltip(List<Component> list) {
        list.add(new TranslatableComponent("gui.refinedrelocation:block_extender.stack_limiter"));
        list.add(formattedTranslation(ChatFormatting.GREEN, "gui.refinedrelocation:block_extender.stack_limiter_increase"));
        list.add(formattedTranslation(ChatFormatting.RED, "gui.refinedrelocation:block_extender.stack_limiter_decrease"));
    }

}
