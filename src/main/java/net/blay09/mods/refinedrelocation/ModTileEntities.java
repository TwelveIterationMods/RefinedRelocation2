package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.block.SortingChestBlock;
import net.blay09.mods.refinedrelocation.tile.*;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModTileEntities {

    public static List<TileEntityType<SortingChestTileEntity>> sortingChests;
    public static TileEntityType<TileBlockExtender> blockExtender;
    public static TileEntityType<FastHopperTileEntity> fastHopper;
    public static TileEntityType<FilteredHopperTileEntity> filteredHopper;
    public static TileEntityType<TileSortingConnector> sortingConnector;
    public static TileEntityType<TileSortingInterface> sortingInterface;

    public static void registerTileEntities(IForgeRegistry<TileEntityType<?>> registry) {
        sortingChests = new ArrayList<>();
        for (SortingChestBlock sortingChest : ModBlocks.sortingChests) {
            TileEntityType<SortingChestTileEntity> tileEntityType = build(() -> new SortingChestTileEntity(sortingChest.getChestType()), sortingChest);
            sortingChests.add(tileEntityType);
            registry.register(tileEntityType);
        }

        registry.registerAll(
                blockExtender = build(TileBlockExtender::new, ModBlocks.blockExtender),
                fastHopper = build(FastHopperTileEntity::new, ModBlocks.fastHopper),
                filteredHopper = build(FilteredHopperTileEntity::new, ModBlocks.filteredHopper),
                sortingConnector = build(TileSortingConnector::new, ModBlocks.sortingConnector),
                sortingInterface = build(TileSortingInterface::new, ModBlocks.sortingInterface)
        );
    }

    @SuppressWarnings("unchecked")
    private static <T extends TileEntity> TileEntityType<T> build(Supplier<T> factory, Block block) {
        ResourceLocation registryName = block.getRegistryName();
        if (registryName == null) {
            throw new IllegalArgumentException("Block passed into tile entity registration is not registered correctly");
        }

        //noinspection ConstantConditions dataFixerType can be null apparently
        return (TileEntityType<T>) TileEntityType.Builder.create(factory, block).build(null).setRegistryName(registryName);
    }

}
