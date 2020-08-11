package net.blay09.mods.refinedrelocation.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class TextUtils {

    public static ITextComponent formattedTranslation(TextFormatting formatting, String langKey, Object... args) {
        final TranslationTextComponent translationTextComponent = new TranslationTextComponent(langKey, args);
        translationTextComponent.mergeStyle(formatting);
        return translationTextComponent;
    }
}
