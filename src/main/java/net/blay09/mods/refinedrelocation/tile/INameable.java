package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.api.INameTaggable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @deprecated INameable will be removed in 1.13. Switch to the NameTaggable capability.
 * 1) Create a default instance of the capability: @code{private final INameTaggable nameTaggable = Capabilities.getDefaultInstance(Capabilities.NAME_TAGGABLE);}
 * 2) Implement @code{getDisplayName()} in the tile entity to delegate to @code{nameTaggable.getDisplayName()}
 * 3) Add the capability to @code{hasCapability()} and @code{getCapability()}
 * 4) Use @code{serializeNBT()} and @code{deserializeNBT()} in @code{writeFromNBT()} and @code{readFromNBT()} to make sure the custom name gets saved.
 * * Use of a name tag on name-taggable tiles is handled by Refined Relocation.
 * * Syncing code must still be implemented individually.
 */
@Deprecated
public interface INameable extends INameTaggable {
    default ITextComponent getDisplayName() {
        return hasCustomName() ? new TextComponentString(getCustomName()) : new TextComponentTranslation(getUnlocalizedName());
    }

    String getUnlocalizedName();
}
