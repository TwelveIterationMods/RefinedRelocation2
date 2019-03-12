package net.blay09.mods.refinedrelocation;

import com.google.common.collect.Lists;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.capability.*;
import net.blay09.mods.refinedrelocation.client.gui.*;
import net.blay09.mods.refinedrelocation.client.render.RenderSortingChest;
import net.blay09.mods.refinedrelocation.compat.RefinedAddon;
import net.blay09.mods.refinedrelocation.container.ContainerRootFilter;
import net.blay09.mods.refinedrelocation.filter.*;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.blay09.mods.refinedrelocation.tile.TileFastHopper;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod.EventBusSubscriber
@Mod(RefinedRelocation.MOD_ID)
public class RefinedRelocation {

    public static final String MOD_ID = "refinedrelocation";

    public static final Logger logger = LogManager.getLogger(MOD_ID);

    public static final ItemGroup itemGroup = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.sortingChest);
        }
    };

    private static final List<RefinedAddon> inbuiltAddons = Lists.newArrayList();

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
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::registerItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, this::registerTileEntities);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::finishLoading);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RefinedRelocationConfig.commonSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, RefinedRelocationConfig.clientSpec);

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> it -> {
            EntityPlayer player = Minecraft.getInstance().player;
            BlockPos pos;
            TileEntity tileEntity;
            switch (it.getId().getPath()) {
                case "block_extender":
                    pos = it.getAdditionalData().readBlockPos();
                    tileEntity = player.world.getTileEntity(pos);
                    if (tileEntity instanceof TileBlockExtender) {
                        EnumFacing clickedFace = EnumFacing.byIndex(it.getAdditionalData().readByte());
                        return new GuiBlockExtender(player, (TileBlockExtender) tileEntity, clickedFace);
                    }
                    break;
                case "sorting_chest":
                    pos = it.getAdditionalData().readBlockPos();
                    tileEntity = player.world.getTileEntity(pos);
                    if (tileEntity instanceof TileSortingChest) {
                        return new GuiSortingChest(player, (TileSortingChest) tileEntity);
                    }
                    break;
                case "fast_hopper":
                    pos = it.getAdditionalData().readBlockPos();
                    tileEntity = player.world.getTileEntity(pos);
                    if (tileEntity instanceof TileFastHopper) {
                        return new GuiFastHopper(player, (TileFastHopper) tileEntity);
                    }
                    break;
                case "root_filter":
                    pos = it.getAdditionalData().readBlockPos();
                    tileEntity = player.world.getTileEntity(pos);
                    if (tileEntity != null && tileEntity.getCapability(CapabilityRootFilter.CAPABILITY).isPresent()) {
                        return new GuiRootFilter(player, tileEntity);
                    }
                    break;
                case "any_filter":
                    pos = it.getAdditionalData().readBlockPos();
                    tileEntity = player.world.getTileEntity(pos);
                    if (tileEntity != null) {
                        Container container = player.openContainer;
                        if (container instanceof ContainerRootFilter) {
                            IFilter filter = ((ContainerRootFilter) container).getRootFilter().getFilter(it.getAdditionalData().readInt());
                            if (filter instanceof IChecklistFilter) {
                                return new GuiChecklistFilter(player, tileEntity, (IChecklistFilter) filter);
                            } else if (filter instanceof NameFilter) {
                                return new GuiNameFilter(player, tileEntity, (NameFilter) filter);
                            }
                        }
                    }
                    break;
                case "block_extender_root_filter":
                    pos = it.getAdditionalData().readBlockPos();
                    tileEntity = player.world.getTileEntity(pos);
                    if (tileEntity instanceof TileBlockExtender) {
                        TileBlockExtender tileBlockExtender = (TileBlockExtender) tileEntity;
                        boolean isOutputFilter = it.getAdditionalData().readInt() == 1;
                        return new GuiRootFilter(new ContainerRootFilter(
                                player,
                                tileEntity,
                                isOutputFilter ? tileBlockExtender.getOutputFilter() : tileBlockExtender.getInputFilter()));
                    }
                    break;
            }

            return null;
        });
    }

    private void registerBlocks(RegistryEvent.Register<Block> event) {
        ModBlocks.register(event.getRegistry());

        for (RefinedAddon addon : inbuiltAddons) {
            addon.registerBlocks(event.getRegistry());
        }
    }

    private void registerItems(RegistryEvent.Register<Item> event) {
        ModItems.register(event.getRegistry());
        ModBlocks.registerItemBlocks(event.getRegistry());

        for (RefinedAddon addon : inbuiltAddons) {
            addon.registerItems(event.getRegistry());
        }
    }

    private void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        ModTiles.registerTileEntities(event.getRegistry());
    }

    private void setup(FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            CapabilitySimpleFilter.register();
            CapabilityRootFilter.register();
            CapabilitySortingGridMember.register();
            CapabilitySortingInventory.register();
            CapabilitySortingUpgradable.register();
        });
    }

    private void setupClient(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileSortingChest.class, new RenderSortingChest());

        for (RefinedAddon addon : inbuiltAddons) {
            addon.setupClient();
        }
    }

    private void finishLoading(FMLLoadCompleteEvent event) {
        CreativeTabFilter.gatherCreativeTabs();
        ModFilter.gatherMods();
    }

    public static List<RefinedAddon> getInbuiltAddons() {
        return inbuiltAddons;
    }
}
