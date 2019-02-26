package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.client.gui.base.ITickableElement;
import net.blay09.mods.refinedrelocation.client.gui.base.ITooltipElement;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class GuiButtonStackLimiter extends GuiButton implements ITickableElement, ITooltipElement {

    private final TileBlockExtender blockExtender;

    public GuiButtonStackLimiter(int buttonId, int x, int y, int width, int height, TileBlockExtender blockExtender) {
        super(buttonId, x, y, width, height, "");
        this.blockExtender = blockExtender;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (isPressable(mouseX, mouseY)) {
            playPressSound(Minecraft.getInstance().getSoundHandler());
            onClick(mouseX, mouseY, mouseButton);
        }

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void onClick(double mouseX, double mouseY, int mouseButton) {
        int limit = blockExtender.getStackLimiterLimit();
        int index = (int) (Math.log(limit) / Math.log(2));
        int maxIndex = (int) (Math.log(Items.AIR.getItemStackLimit(ItemStack.EMPTY) / Math.log(2)));
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
    public boolean mouseScrolled(double delta) {
        int limit = blockExtender.getStackLimiterLimit();
        if (delta > 0) {
            limit++;
        } else if (delta < 0) {
            limit--;
        }

        limit = MathHelper.clamp(limit, 1, Items.AIR.getItemStackLimit(ItemStack.EMPTY));
        blockExtender.setStackLimiterLimit(limit);
        return true;
    }

    @Override
    public void tick() {
        displayString = String.valueOf(blockExtender.getStackLimiterLimit());
    }

    @Override
    public void addTooltip(List<String> list) {
        list.add(I18n.format("gui.refinedrelocation:block_extender.stack_limiter"));
        list.add(TextFormatting.GREEN + I18n.format("gui.refinedrelocation:block_extender.stack_limiter_increase"));
        list.add(TextFormatting.RED + I18n.format("gui.refinedrelocation:block_extender.stack_limiter_decrease"));
    }

}
