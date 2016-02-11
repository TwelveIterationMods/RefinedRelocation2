package net.blay09.mods.refinedrelocation2;

import net.blay09.mods.refinedrelocation2.api.capability.IFilterProvider;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingInventory;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingGridMember;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "refinedrelocation2", name = "Refined Relocation 2", version = "{version}")
public class RefinedRelocation2 {

    @CapabilityInject(ISortingGridMember.class)
    public static Capability<ISortingGridMember> SORTING_GRID_MEMBER = null;

    @CapabilityInject(ISortingInventory.class)
    public static Capability<ISortingInventory> SORTING_INVENTORY = null;

    @CapabilityInject(IFilterProvider.class)
    public static Capability<IFilterProvider> FILTER_PROVIDER = null;

    @CapabilityInject(IItemHandler.class)
    public static Capability<IItemHandler> ITEM_HANDLER = null;

    public static final String MOD_ID = "refinedrelocation2";

    @Mod.Instance
    public static RefinedRelocation2 instance;

    public static final Logger logger = LogManager.getLogger();

    @SidedProxy(clientSide = "net.blay09.mods.refinedrelocation2.client.ClientProxy", serverSide = "net.blay09.mods.refinedrelocation2.CommonProxy")
    public static CommonProxy proxy;

    public static CreativeTabs creativeTab = new CreativeTabs(MOD_ID) {
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(ModBlocks.sortingChest);
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

}
