package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.SortingChestType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class ModBlocks {

    public static SortingChestBlock[] sortingChests;
    public static Block blockExtender =new BlockExtenderBlock();
    public static Block fastHopper = new FastHopperBlock();
    public static Block filteredHopper = new FilteredHopperBlock();
    public static Block sortingConnector = new SortingConnectorBlock();
    public static Block sortingInterface = new SortingInterfaceBlock();

    public static void initialize(BalmBlocks blocks) {
        SortingChestType[] chestTypes = SortingChestType.values();
        sortingChests = new SortingChestBlock[chestTypes.length];
        for (SortingChestType type : chestTypes) {
            sortingChests[type.ordinal()] = new SortingChestBlock(type);
            blocks.register(() -> sortingChests[type.ordinal()], () -> itemBlock(sortingChests[type.ordinal()]), id(type.getRegistryName()));
        }

        blocks.register(() -> blockExtender, () -> itemBlock(blockExtender), id("block_extender"));
        blocks.register(() -> fastHopper, () -> itemBlock(fastHopper), id("fast_hopper"));
        blocks.register(() -> filteredHopper, () -> itemBlock(filteredHopper), id("filtered_hopper"));
        blocks.register(() -> sortingConnector, () -> itemBlock(sortingConnector), id("sorting_connector"));
        blocks.register(() -> sortingInterface, () -> itemBlock(sortingInterface), id("sorting_interface"));
    }

    private static BlockItem itemBlock(Block block) {
        return new BlockItem(block, Balm.getItems().itemProperties());
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(RefinedRelocation.MOD_ID, path);
    }

}
