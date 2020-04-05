package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.client.gui.base.ITickableElement;
import net.blay09.mods.refinedrelocation.client.gui.base.ITooltipElement;
import net.blay09.mods.refinedrelocation.client.gui.base.element.SizableButton;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class GuiButtonStackLimiter extends SizableButton implements ITickableElement, ITooltipElement {

    private final TileBlockExtender blockExtender;

    public GuiButtonStackLimiter(int x, int y, int width, int height, TileBlockExtender blockExtender) {
        super(x, y, width, height, "", it -> {
        });
        this.blockExtender = blockExtender;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (isMouseOver(mouseX, mouseY)) {
            playDownSound(Minecraft.getInstance().getSoundHandler());
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

        limit = MathHelper.clamp(limit, 1, Items.AIR.getItemStackLimit(ItemStack.EMPTY));
        blockExtender.setStackLimiterLimit(limit);
        return true;
    }

    @Override
    public void tick() {
        setMessage(String.valueOf(blockExtender.getStackLimiterLimit()));
    }

    @Override
    public void addTooltip(List<String> list) {
        list.add(I18n.format("gui.refinedrelocation:block_extender.stack_limiter"));
        list.add(TextFormatting.GREEN + I18n.format("gui.refinedrelocation:block_extender.stack_limiter_increase"));
        list.add(TextFormatting.RED + I18n.format("gui.refinedrelocation:block_extender.stack_limiter_decrease"));
    }


}
