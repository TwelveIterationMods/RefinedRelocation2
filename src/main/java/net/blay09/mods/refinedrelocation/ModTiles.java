package net.blay09.mods.refinedrelocation;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import net.blay09.mods.refinedrelocation.block.*;
import net.blay09.mods.refinedrelocation.tile.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ModTiles {

    public static TileEntityType<SortingChestTileEntity> sortingChest;
    public static TileEntityType<TileBlockExtender> blockExtender;
    public static TileEntityType<TileFastHopper> fastHopper;
    public static TileEntityType<TileFilteredHopper> filteredHopper;
    public static TileEntityType<TileSortingConnector> sortingConnector;
    public static TileEntityType<TileSortingInterface> sortingInterface;

    public static void registerTileEntities(IForgeRegistry<TileEntityType<?>> registry) {
        registry.registerAll(
                sortingChest = build(SortingChestTileEntity::new, SortingChestBlock.registryName),
                blockExtender = build(TileBlockExtender::new, BlockBlockExtender.registryName),
                fastHopper = build(TileFastHopper::new, BlockFastHopper.registryName),
                filteredHopper = build(TileFilteredHopper::new, BlockFilteredHopper.registryName),
                sortingConnector = build(TileSortingConnector::new, BlockSortingConnector.registryName),
                sortingInterface = build(TileSortingInterface::new, BlockSortingInterface.registryName)
        );
    }

    @SuppressWarnings("unchecked")
    private static <T extends TileEntity> TileEntityType<T> build(Supplier<T> factory, ResourceLocation registryName) {
        //noinspection ConstantConditions
        return (TileEntityType<T>) TileEntityType.Builder.create(factory).build(dataFixerType(registryName)).setRegistryName(registryName);
    }

    @Nullable
    private static Type<?> dataFixerType(ResourceLocation registryName) {
        try {
            return DataFixesManager.getDataFixer()
                    .getSchema(DataFixUtils.makeKey(1519))
                    .getChoiceType(TypeReferences.BLOCK_ENTITY, registryName.toString());
        } catch (IllegalArgumentException e) {
            if (SharedConstants.developmentMode) {
                throw e;
            }
        }
        return null;
    }

}
