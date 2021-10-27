package net.blay09.mods.refinedrelocation.block.entity;

import net.minecraftforge.items.IItemHandler;

import java.util.Collection;

public interface IDroppableItemHandler {
    Collection<IItemHandler> getDroppedItemHandlers();
}
