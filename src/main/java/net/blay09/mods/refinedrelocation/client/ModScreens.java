package net.blay09.mods.refinedrelocation.client;

import net.blay09.mods.refinedrelocation.client.gui.*;
import net.blay09.mods.refinedrelocation.container.ModContainers;
import net.minecraft.client.gui.ScreenManager;

public class ModScreens {
    public static void register() {
        ScreenManager.registerFactory(ModContainers.blockExtender, BlockExtenderScreen::new);
        ScreenManager.registerFactory(ModContainers.fastHopper, FastHopperScreen::new);
        ScreenManager.registerFactory(ModContainers.sortingChest, SortingChestScreen::new);
        ScreenManager.registerFactory(ModContainers.nameFilter, NameFilterScreen::new);
        ScreenManager.registerFactory(ModContainers.checklistFilter, ChecklistFilterScreen::new);
        ScreenManager.registerFactory(ModContainers.rootFilter, RootFilterScreen::new);
        ScreenManager.registerFactory(ModContainers.addFilter, AddFilterScreen::new);
        ScreenManager.registerFactory(ModContainers.blockExtenderInputFilter, RootFilterScreen::new);
        ScreenManager.registerFactory(ModContainers.blockExtenderOutputFilter, RootFilterScreen::new);
    }
}
