package net.blay09.mods.refinedrelocation.client.gui.element;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.blay09.mods.refinedrelocation.client.gui.base.ITooltipElement;
import net.blay09.mods.refinedrelocation.client.gui.base.element.ImageButton;
import net.blay09.mods.refinedrelocation.menu.BlockExtenderMenu;
import net.blay09.mods.refinedrelocation.block.entity.BlockExtenderBlockEntity;
import net.blay09.mods.refinedrelocation.util.RelativeSide;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.blay09.mods.refinedrelocation.util.TextUtils.formattedTranslation;

public class SideToggleButton extends ImageButton implements ITooltipElement {

    private final BlockExtenderBlockEntity tileEntity;
    private final RelativeSide side;

    public SideToggleButton(int x, int y, BlockExtenderBlockEntity tileEntity, RelativeSide side) {
        super(x, y, 16, 16, GuiTextures.SIDE_BUTTONS[side.ordinal()], it -> {
        });
        this.tileEntity = tileEntity;
        this.side = side;
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
        if (side == RelativeSide.FRONT) {
            if (Screen.hasShiftDown()) {
                for (RelativeSide side : RelativeSide.values()) {
                    if (side != RelativeSide.FRONT) {
                        tileEntity.setSideMapping(side, null);
                        RefinedRelocationAPI.sendContainerMessageToServer(BlockExtenderMenu.KEY_TOGGLE_SIDE, side.ordinal(), -1);
                    }
                }
            }
            return;
        }

        Direction facing = tileEntity.getSideMapping(side);
        int index;
        if (mouseButton == 0) {
            index = facing != null ? facing.get3DDataValue() + 1 : 0;
        } else if (mouseButton == 1) {
            index = facing != null ? facing.get3DDataValue() - 1 : 5;
        } else {
            return;
        }
        if (index >= 6) {
            facing = null;
        } else if (index < 0) {
            facing = null;
        } else {
            facing = Direction.from3DDataValue(index);
        }

        tileEntity.setSideMapping(side, facing);
        RefinedRelocationAPI.sendContainerMessageToServer(BlockExtenderMenu.KEY_TOGGLE_SIDE, side.ordinal(), facing != null ? facing.get3DDataValue() : -1);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        if (side != RelativeSide.FRONT) {
            Font font = Minecraft.getInstance().font;
            char sideChar = getFacingChar(tileEntity.getSideMapping(side));
            int labelX = (int) (getX() + width / 2f - font.width(String.valueOf(sideChar)) / 2f);
            int labelY = (int) (getY() + height / 2f - font.lineHeight / 2f);
            final var poseStack = guiGraphics.pose();
            poseStack.translate(0.5f, 0.5f, 0);
            guiGraphics.drawString(font, String.valueOf(sideChar), labelX, labelY, 0xFFFFFFFF);
            poseStack.translate(-0.5f, -0.5f, 0);
        }
    }

    @Override
    public void addTooltip(List<Component> list) {
        if (side == RelativeSide.FRONT) {
            list.add(formattedTranslation(ChatFormatting.RED, "gui.refinedrelocation:block_extender.front"));
        } else {
            Direction mapping = tileEntity.getSideMapping(side);
            list.add(formattedTranslation(ChatFormatting.YELLOW, "gui.refinedrelocation:block_extender.side_tooltip", formattedTranslation(ChatFormatting.WHITE, "gui.refinedrelocation:block_extender.side_" + (mapping != null ? mapping.getSerializedName() : "none"))));
            list.add(formattedTranslation(ChatFormatting.RED, "gui.refinedrelocation:block_extender.toggle_side"));
        }
    }

    private char getFacingChar(@Nullable Direction facing) {
        if (facing == null) {
            return '-';
        }
        return switch (facing) {
            case DOWN -> 'D';
            case UP -> 'U';
            case WEST -> 'W';
            case EAST -> 'E';
            case NORTH -> 'N';
            case SOUTH -> 'S';
        };
    }

}
