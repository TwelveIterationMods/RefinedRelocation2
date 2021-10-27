package net.blay09.mods.refinedrelocation.item;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.SortingChestType;
import net.blay09.mods.refinedrelocation.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModItems {
    public static final CreativeModeTab creativeModeTab = Balm.getItems().createCreativeModeTab(id("refinedrelocation"), () -> new ItemStack(ModBlocks.sortingChests[SortingChestType.WOOD.ordinal()]));

    public static Item sortingUpgrade = new SortingUpgradeItem();
    public static Item stackLimiter = new StackLimiterItem();
    public static Item inputFilter = new InputFilterItem();
    public static Item outputFilter = new OutputFilterItem();
    public static Item slotLock = new SlotLockItem();

    public static void initialize(BalmItems items) {
        items.registerItem(() -> sortingUpgrade, id("sorting_upgrade"));
        items.registerItem(() -> stackLimiter, id("stack_limiter"));
        items.registerItem(() -> inputFilter, id("input_filter"));
        items.registerItem(() -> outputFilter, id("output_filter"));
        items.registerItem(() -> slotLock, id("slot_lock"));
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(RefinedRelocation.MOD_ID, path);
    }
}
