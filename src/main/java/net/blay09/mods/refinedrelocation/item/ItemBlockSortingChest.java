package net.blay09.mods.refinedrelocation.item;

import net.blay09.mods.refinedrelocation.client.render.RenderSortingChest;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockSortingChest extends ItemBlock {
    public ItemBlockSortingChest(Block block, Properties builder) {
        super(block, builder.setTEISR(() -> () -> RenderSortingChest.sortingChestItemRenderer));
    }
}
