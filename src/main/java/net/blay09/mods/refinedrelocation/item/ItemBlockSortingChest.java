package net.blay09.mods.refinedrelocation.item;

import net.blay09.mods.refinedrelocation.client.render.SortingChestTileEntityRenderer;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class ItemBlockSortingChest extends BlockItem {
    public ItemBlockSortingChest(Block block, Properties builder) {
        super(block, builder.setTEISR(() -> () -> SortingChestTileEntityRenderer.sortingChestItemRenderer));
    }
}
