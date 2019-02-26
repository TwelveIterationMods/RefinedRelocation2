package net.blay09.mods.refinedrelocation.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IInteractionObject;

import javax.annotation.Nullable;

public interface IInteractionObjectWithoutName extends IInteractionObject {
    @Override
    default ITextComponent getName() {
        return new TextComponentString(getClass().getName());
    }

    @Override
    default boolean hasCustomName() {
        return false;
    }

    @Nullable
    @Override
    default ITextComponent getCustomName() {
        return null;
    }
}
