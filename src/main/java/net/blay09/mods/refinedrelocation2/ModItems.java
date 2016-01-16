package net.blay09.mods.refinedrelocation2;

import net.blay09.mods.refinedrelocation2.item.*;
import net.blay09.mods.refinedrelocation2.item.toolbox.ItemToolbox;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

    public static ItemSortingConnector sortingConnector;
    public static ItemSortingInterface sortingInterface;
    public static ItemInputPane inputPane;
    public static ItemSortingUpgrade sortingUpgrade;
    public static ItemToolbox toolbox;

    public static void register() {
        sortingConnector = new ItemSortingConnector();
        sortingInterface = new ItemSortingInterface();
//        inputPane = new ItemInputPane();
//        sortingUpgrade = new ItemSortingUpgrade();
        toolbox = new ItemToolbox();
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels(ItemModelMesher mesher) {
        sortingConnector.registerModels(mesher);
        sortingInterface.registerModels(mesher);
//        sortingUpgrade.registerModels(mesher);
//        inputPane.registerModels(mesher);
        toolbox.registerModels(mesher);
    }

}
