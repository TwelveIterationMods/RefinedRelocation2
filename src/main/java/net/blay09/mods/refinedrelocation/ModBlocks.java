package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.block.*;
import net.blay09.mods.refinedrelocation.item.ItemBlockSortingChest;
import net.blay09.mods.refinedrelocation.tile.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    public static Block sortingChest;
    public static Block blockExtender;
    public static Block fastHopper;
    public static Block filteredHopper;
    public static Block sortingConnector;
    public static Block sortingInterface;

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                sortingChest = new BlockSortingChest().setRegistryName(BlockSortingChest.name),
                blockExtender = new BlockBlockExtender().setRegistryName(BlockBlockExtender.name),
                fastHopper = new BlockFastHopper().setRegistryName(BlockFastHopper.name),
                filteredHopper = new BlockFilteredHopper().setRegistryName(BlockFilteredHopper.name),
                sortingConnector = new BlockSortingConnector().setRegistryName(BlockSortingConnector.name),
                sortingInterface = new BlockSortingInterface().setRegistryName(BlockSortingInterface.name)
        );
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                new ItemBlockSortingChest(sortingChest, itemBlockProperties()).setRegistryName(BlockSortingChest.name),
                new ItemBlock(blockExtender, itemBlockProperties()).setRegistryName(BlockBlockExtender.name),
                new ItemBlock(fastHopper, itemBlockProperties()).setRegistryName(BlockFastHopper.name),
                new ItemBlock(filteredHopper, itemBlockProperties()).setRegistryName(BlockFilteredHopper.name),
                new ItemBlock(sortingConnector, itemBlockProperties()).setRegistryName(BlockSortingConnector.name),
                new ItemBlock(sortingInterface, itemBlockProperties()).setRegistryName(BlockSortingInterface.name)
        );
    }

    public static Item.Properties itemBlockProperties() {
        return new Item.Properties().group(RefinedRelocation.itemGroup);
    }

}
