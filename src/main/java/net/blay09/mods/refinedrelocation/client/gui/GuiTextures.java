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

    public static final GuiTextureSpriteButton DELETE_FILTER = null;
    public static final GuiTextureSpriteButton OPEN_FILTER = null;
    public static final GuiTextureSpriteButton RETURN_FROM_FILTER = null;
    public static final GuiTextureSpriteButton FILTER_WHITELIST = null;
    public static final GuiTextureSpriteButton FILTER_BLACKLIST = null;
    public static final IDrawable PRESET_FILTER_ICON = null;
    public static final IDrawable SCROLLBAR_TOP = null;
    public static final IDrawable SCROLLBAR_MIDDLE = null;
    public static final IDrawable SCROLLBAR_BOTTOM = null;
    public static final IDrawable CHECKLIST = null;
    public static final IDrawable CHECKLIST_CHECKED = null;
    public static final IDrawable FILTER_SLOT = null;
}
