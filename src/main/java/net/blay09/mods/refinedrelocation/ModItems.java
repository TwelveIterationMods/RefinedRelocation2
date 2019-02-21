package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.item.*;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {
    public static Item sortingUpgrade;
    public static Item stackLimiter;
    public static Item inputFilter;
    public static Item outputFilter;
    public static Item slotLock;

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                sortingUpgrade = new ItemSortingUpgrade().setRegistryName(ItemSortingUpgrade.name),
                stackLimiter = new ItemStackLimiter().setRegistryName(ItemStackLimiter.name),
                inputFilter = new ItemInputFilter().setRegistryName(ItemInputFilter.name),
                outputFilter = new ItemOutputFilter().setRegistryName(ItemOutputFilter.name),
                slotLock = new ItemSlotLock().setRegistryName(ItemSlotLock.name)
        );
    }

}
