package net.blay09.mods.refinedrelocation.client.gui;

import net.blay09.mods.refinedrelocation.api.client.IDrawable;
import net.blay09.mods.refinedrelocation.client.gui.base.GuiTextureSprite;
import net.blay09.mods.refinedrelocation.client.gui.base.GuiTextureSpriteButton;

public class GuiTextures {

    public static final GuiTextureSprite NONE = new GuiTextureSprite() {
        @Override
        public void draw(double x, double y, double zLevel) {
            // NOP
        }

        @Override
        public void draw(double x, double y, double width, double height, double zLevel) {
            // NOP
        }
    };

    public static final GuiTextureSpriteButton BUTTON_NONE = new GuiTextureSpriteButton(NONE, NONE, NONE);
    public static final GuiTextureSpriteButton DELETE_FILTER = BUTTON_NONE;
    public static final GuiTextureSpriteButton OPEN_FILTER = BUTTON_NONE;
    public static final GuiTextureSpriteButton CHEST_BUTTON = BUTTON_NONE;
    public static final GuiTextureSpriteButton FILTER_WHITELIST = BUTTON_NONE;
    public static final GuiTextureSpriteButton FILTER_BLACKLIST = BUTTON_NONE;
    public static final IDrawable PRESET_FILTER_ICON = NONE;
    public static final IDrawable SCROLLBAR_TOP = NONE;
    public static final IDrawable SCROLLBAR_MIDDLE = NONE;
    public static final IDrawable SCROLLBAR_BOTTOM = NONE;
    public static final IDrawable CHECKLIST = NONE;
    public static final IDrawable CHECKLIST_CHECKED = NONE;
    public static final IDrawable FILTER_SLOT = NONE;
    public static final IDrawable FILTER_SEPARATOR = NONE;
    public static final IDrawable NAME_FILTER_ICON = NONE;
    public static final IDrawable CREATIVE_TAB_FILTER_ICON = NONE;
    public static final IDrawable MOD_FILTER_ICON = NONE;
    public static final IDrawable SAME_ITEM_FILTER_ICON = NONE;
    public static final IDrawable SAME_MOD_FILTER_ICON = NONE;
}
