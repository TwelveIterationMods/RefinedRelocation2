package net.blay09.mods.refinedrelocation;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import net.blay09.mods.refinedrelocation.tile.*;
import net.minecraft.block.Block;
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
                sortingChest = build(SortingChestTileEntity::new, ModBlocks.sortingChest),
                blockExtender = build(TileBlockExtender::new, ModBlocks.blockExtender),
                fastHopper = build(TileFastHopper::new, ModBlocks.fastHopper),
                filteredHopper = build(TileFilteredHopper::new, ModBlocks.filteredHopper),
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

        Type<?> dataFixerType = dataFixerType(registryName);
        if (dataFixerType == null) {
            throw new IllegalArgumentException("Could not create data fixer for tile entity registration of " + registryName);
        }

        return (TileEntityType<T>) TileEntityType.Builder.create(factory, block).build(dataFixerType).setRegistryName(registryName);
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
