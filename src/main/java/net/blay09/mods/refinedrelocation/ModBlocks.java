package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.block.*;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

public class ModBlocks {

    public static SortingChestBlock[] sortingChests;
    public static Block blockExtender;
    public static Block fastHopper;
    public static Block filteredHopper;
    public static Block sortingConnector;
    public static Block sortingInterface;

    public static void register(IForgeRegistry<Block> registry) {
        SortingChestType[] chestTypes = SortingChestType.values();
        sortingChests = new SortingChestBlock[chestTypes.length];
        for (SortingChestType type : chestTypes) {
            SortingChestBlock sortingChest = new SortingChestBlock(type);
            sortingChest.setRegistryName(type.getRegistryName());
            registry.register(sortingChests[type.ordinal()] = sortingChest);
        }

        registry.registerAll(
                blockExtender = new BlockExtenderBlock().setRegistryName(BlockExtenderBlock.name),
                fastHopper = new FastHopperBlock().setRegistryName(FastHopperBlock.name),
                filteredHopper = new FilteredHopperBlock().setRegistryName(FilteredHopperBlock.name),
                sortingConnector = new SortingConnectorBlock().setRegistryName(SortingConnectorBlock.name),
                sortingInterface = new SortingInterfaceBlock().setRegistryName(SortingInterfaceBlock.name)
        );
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        for (Block sortingChest : sortingChests) {
            registry.register(new BlockItem(sortingChest, itemBlockProperties()).setRegistryName(Objects.requireNonNull(sortingChest.getRegistryName())));
        }

        registry.registerAll(
                new BlockItem(blockExtender, itemBlockProperties()).setRegistryName(BlockExtenderBlock.name),
                new BlockItem(fastHopper, itemBlockProperties()).setRegistryName(FastHopperBlock.name),
                new BlockItem(filteredHopper, itemBlockProperties()).setRegistryName(FilteredHopperBlock.name),
                new BlockItem(sortingConnector, itemBlockProperties()).setRegistryName(SortingConnectorBlock.name),
                new BlockItem(sortingInterface, itemBlockProperties()).setRegistryName(SortingInterfaceBlock.name)
        );
    }

    private static Item.Properties itemBlockProperties() {
        return new Item.Properties().group(RefinedRelocation.itemGroup);
    }

}
