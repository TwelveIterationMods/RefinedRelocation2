package net.blay09.mods.refinedrelocation2;

import net.blay09.mods.refinedrelocation2.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingInventory;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingGridMember;
import net.blay09.mods.refinedrelocation2.capability.SortingGridMemberDefaultImpl;
import net.blay09.mods.refinedrelocation2.capability.SortingInventoryDefaultImpl;
import net.blay09.mods.refinedrelocation2.capability.SortingInventoryStorage;
import net.blay09.mods.refinedrelocation2.capability.SortingMemberStorage;
import net.blay09.mods.refinedrelocation2.network.GuiHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = "refinedrelocation2", name = "Refined Relocation 2", version = "{version}")
public class RefinedRelocation2 {

    @CapabilityInject(ISortingGridMember.class)
    public static Capability<ISortingGridMember> SORTING_GRID_MEMBER = null;

    @CapabilityInject(ISortingInventory.class)
    public static Capability<ISortingInventory> SORTING_INVENTORY = null;

    public static final String MOD_ID = "refinedrelocation2";

    @Mod.Instance
    public static RefinedRelocation2 instance;

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
        RefinedRelocationAPI.setupAPI(new InternalMethods());
        CapabilityManager.INSTANCE.register(ISortingGridMember.class, new SortingMemberStorage(), SortingGridMemberDefaultImpl.class);
        CapabilityManager.INSTANCE.register(ISortingInventory.class, new SortingInventoryStorage(), SortingInventoryDefaultImpl.class);

        MinecraftForge.EVENT_BUS.register(this);

        ModBlocks.init();
        ModItems.init();

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }


}
