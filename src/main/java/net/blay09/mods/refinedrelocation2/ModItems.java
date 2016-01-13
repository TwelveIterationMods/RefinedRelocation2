package net.blay09.mods.refinedrelocation2;

import mcmultipart.multipart.MultipartRegistry;
import net.blay09.mods.refinedrelocation2.item.*;
import net.blay09.mods.refinedrelocation2.part.PartInputPane;
import net.blay09.mods.refinedrelocation2.part.PartSortingConnector;
import net.blay09.mods.refinedrelocation2.part.PartSortingInterface;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

    public static ItemSortingConnector sortingConnector;
    public static ItemSortingInterface sortingInterface;
    public static ItemSortingUpgrade sortingUpgrade;
    public static ItemInputPane inputPane;

    public static void init() {
        sortingConnector = new ItemSortingConnector();
        MultipartRegistry.registerPartFactory((s, b) -> {
            return new PartSortingConnector();
        }, sortingConnector.getRegistryName());
        GameRegistry.registerItem(sortingConnector);

        sortingInterface = new ItemSortingInterface();
        MultipartRegistry.registerPartFactory((s, b) -> {
            return new PartSortingInterface();
        }, sortingInterface.getRegistryName());
        GameRegistry.registerItem(sortingInterface);

        sortingUpgrade = new ItemSortingUpgrade();
        GameRegistry.registerItem(sortingUpgrade);

        inputPane = new ItemInputPane();
        MultipartRegistry.registerPartFactory((s, b) -> {
            return new PartInputPane();
        }, inputPane.getRegistryName());
        GameRegistry.registerItem(inputPane);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels(ItemModelMesher mesher) {
        sortingConnector.registerModels(mesher);
        sortingInterface.registerModels(mesher);
        sortingUpgrade.registerModels(mesher);
        inputPane.registerModels(mesher);
    }

}
