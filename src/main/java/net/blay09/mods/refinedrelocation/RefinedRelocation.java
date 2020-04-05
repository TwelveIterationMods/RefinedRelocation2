package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.capability.*;
import net.blay09.mods.refinedrelocation.client.ModRenderers;
import net.blay09.mods.refinedrelocation.client.ModScreens;
import net.blay09.mods.refinedrelocation.container.ModContainers;
import net.blay09.mods.refinedrelocation.filter.*;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber
@Mod(RefinedRelocation.MOD_ID)
public class RefinedRelocation {

    public static final String MOD_ID = "refinedrelocation";

    public static final Logger logger = LogManager.getLogger(MOD_ID);

    public static final ItemGroup itemGroup = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.sortingChests[SortingChestType.WOOD.ordinal()]);
        }
    };

    public RefinedRelocation() {
        RefinedRelocationAPI.__internal__setupAPI(new InternalMethodsImpl());

        NetworkHandler.init();

        RefinedRelocationAPI.registerFilter(SameItemFilter.class);
        RefinedRelocationAPI.registerFilter(NameFilter.class);
        RefinedRelocationAPI.registerFilter(PresetFilter.class);
        RefinedRelocationAPI.registerFilter(CreativeTabFilter.class);
        RefinedRelocationAPI.registerFilter(ModFilter.class);
        RefinedRelocationAPI.registerFilter(SameModFilter.class);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, this::registerBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, this::registerContainers);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::registerItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, this::registerTileEntities);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::finishLoading);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RefinedRelocationConfig.commonSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, RefinedRelocationConfig.clientSpec);
    }

    private void registerBlocks(RegistryEvent.Register<Block> event) {
        ModBlocks.register(event.getRegistry());
    }

    private void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        ModContainers.register(event.getRegistry());
    }

    private void registerItems(RegistryEvent.Register<Item> event) {
        ModItems.register(event.getRegistry());
        ModBlocks.registerItemBlocks(event.getRegistry());
    }

    private void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        ModTileEntities.registerTileEntities(event.getRegistry());
    }

    private void setup(FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            CapabilitySimpleFilter.register();
            CapabilityRootFilter.register();
            CapabilityMultiRootFilter.register();
            CapabilitySortingGridMember.register();
            CapabilitySortingInventory.register();
            CapabilitySortingUpgradable.register();
        });
    }

    private void setupClient(FMLClientSetupEvent event) {
        ModScreens.register();
        ModRenderers.register();
    }

    private void finishLoading(FMLLoadCompleteEvent event) {
        CreativeTabFilter.gatherCreativeTabs();
        ModFilter.gatherMods();

        if (ModList.get().isLoaded("ironchest")) {
            try {
                Class.forName("net.blay09.mods.refinedrelocation.compat.ironchest.IronChestAddon").newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                logger.error("Failed to load Iron Chest compat", e);
            }
        }
    }

}
