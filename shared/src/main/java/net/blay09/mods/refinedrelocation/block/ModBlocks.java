package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.SortingChestType;
import net.blay09.mods.refinedrelocation.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
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
            blocks.register(() -> sortingChests[type.ordinal()], () -> new BlockItem(sortingChests[type.ordinal()], itemProperties()), id(type.getRegistryName()));
        }

        blocks.register(() -> blockExtender, () -> new BlockItem(blockExtender, itemProperties()), id("block_extender"));
        blocks.register(() -> fastHopper, () -> new BlockItem(fastHopper, itemProperties()), id("fast_hopper"));
        blocks.register(() -> filteredHopper, () -> new BlockItem(filteredHopper, itemProperties()), id("filtered_hopper"));
        blocks.register(() -> sortingConnector, () -> new BlockItem(sortingConnector, itemProperties()), id("sorting_connector"));
        blocks.register(() -> sortingInterface, () -> new BlockItem(sortingInterface, itemProperties()), id("sorting_interface"));
    }

    private static Item.Properties itemProperties() {
        return new Item.Properties().tab(ModItems.creativeModeTab);
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(RefinedRelocation.MOD_ID, path);
    }

}
