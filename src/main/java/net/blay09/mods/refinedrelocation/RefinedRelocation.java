package net.blay09.mods.refinedrelocation;

import com.google.common.collect.Lists;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.capability.*;
import net.blay09.mods.refinedrelocation.client.render.RenderSortingChest;
import net.blay09.mods.refinedrelocation.compat.Compat;
import net.blay09.mods.refinedrelocation.compat.RefinedAddon;
import net.blay09.mods.refinedrelocation.filter.*;
import net.blay09.mods.refinedrelocation.network.GuiHandler;
import net.blay09.mods.refinedrelocation.network.LoginSyncHandler;
import net.blay09.mods.refinedrelocation.network.NetworkHandler;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
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

        ModBlocks.registerTileEntities();

        MinecraftForge.EVENT_BUS.register(new LoginSyncHandler());
        MinecraftForge.EVENT_BUS.register(new BlockRightClickHandler());

        CapabilitySimpleFilter.register();
        CapabilityRootFilter.register();
        CapabilitySortingGridMember.register();
        CapabilitySortingInventory.register();
        CapabilitySortingUpgradable.register();
        CapabilityNameTaggable.register();

        RefinedRelocationAPI.registerFilter(SameItemFilter.class);
        RefinedRelocationAPI.registerFilter(NameFilter.class);
        RefinedRelocationAPI.registerFilter(PresetFilter.class);
        RefinedRelocationAPI.registerFilter(CreativeTabFilter.class);
        RefinedRelocationAPI.registerFilter(ModFilter.class);
        RefinedRelocationAPI.registerFilter(SameModFilter.class);

        RefinedRelocationAPI.registerGuiHandler(TileSortingChest.class, (player, tileEntity) -> RefinedRelocation.proxy.openGui(player, new MessageOpenGui(GuiHandler.GUI_SORTING_CHEST, tileEntity.getPos())));
        RefinedRelocationAPI.registerGuiHandler(TileBlockExtender.class, (player, tileEntity) -> RefinedRelocation.proxy.openGui(player, new MessageOpenGui(GuiHandler.GUI_BLOCK_EXTENDER, tileEntity.getPos())));

        if (ModList.get().isLoaded(Compat.IRONCHEST)) {
            try {
                inbuiltAddons.add((RefinedAddon) Class.forName("net.blay09.mods.refinedrelocation.compat.ironchest.IronChestAddon").newInstance());
            } catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
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

    private void setupClient(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileSortingChest.class, new RenderSortingChest());
    }

    private void finishLoading(FMLLoadCompleteEvent event) {
        CreativeTabFilter.gatherCreativeTabs();
        ModFilter.gatherMods();
    }

    public static List<RefinedAddon> getInbuiltAddons() {
        return inbuiltAddons;
    }
}
