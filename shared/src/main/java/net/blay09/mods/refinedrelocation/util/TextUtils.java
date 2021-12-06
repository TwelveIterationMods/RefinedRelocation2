package net.blay09.mods.refinedrelocation.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class TextUtils {

    public static Component formattedTranslation(ChatFormatting formatting, String langKey, Object... args) {
        final TranslatableComponent translationTextComponent = new TranslatableComponent(langKey, args);
        translationTextComponent.withStyle(formatting);
        return translationTextComponent;
    }
}
