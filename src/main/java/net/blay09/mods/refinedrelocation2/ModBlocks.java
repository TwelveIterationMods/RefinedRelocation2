package net.blay09.mods.refinedrelocation2;

import net.blay09.mods.refinedrelocation2.block.BlockBetterHopper;
import net.blay09.mods.refinedrelocation2.block.BlockExtender;
import net.blay09.mods.refinedrelocation2.block.BlockFilteredHopper;
import net.blay09.mods.refinedrelocation2.block.BlockSortingChest;
import net.blay09.mods.refinedrelocation2.client.render.TileSortingChestRenderer;
import net.blay09.mods.refinedrelocation2.tile.TileBetterHopper;
import net.blay09.mods.refinedrelocation2.tile.TileBlockExtender;
import net.blay09.mods.refinedrelocation2.tile.TileFilteredHopper;
import net.blay09.mods.refinedrelocation2.tile.TileSortingChest;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

    public static BlockSortingChest sortingChest;
    public static BlockBetterHopper betterHopper;
    public static BlockFilteredHopper filteredHopper;
    public static BlockExtender blockExtender;

    public static void register() {
        sortingChest = new BlockSortingChest();
        GameRegistry.registerTileEntity(TileSortingChest.class, sortingChest.getRegistryName());

        betterHopper = new BlockBetterHopper();
        GameRegistry.registerTileEntity(TileBetterHopper.class, betterHopper.getRegistryName());

        filteredHopper = new BlockFilteredHopper();
        GameRegistry.registerTileEntity(TileFilteredHopper.class, filteredHopper.getRegistryName());

        blockExtender = new BlockExtender();
        GameRegistry.registerTileEntity(TileBlockExtender.class, blockExtender.getRegistryName());

    }

    @SideOnly(Side.CLIENT)
    public static void registerSpecialModels() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileSortingChest.class, new TileSortingChestRenderer());
        sortingChest.registerSpecialModels();
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels(ItemModelMesher mesher) {
        betterHopper.registerModels(mesher);
        filteredHopper.registerModels(mesher);
        blockExtender.registerModels(mesher);
    }
}
