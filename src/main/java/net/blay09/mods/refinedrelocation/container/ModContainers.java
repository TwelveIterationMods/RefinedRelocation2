package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.blay09.mods.refinedrelocation.filter.NameFilter;
import net.blay09.mods.refinedrelocation.tile.FastHopperTileEntity;
import net.blay09.mods.refinedrelocation.tile.SortingChestTileEntity;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.IForgeRegistry;

public class ModContainers {
    public static ContainerType<BlockExtenderContainer> blockExtender;
    public static ContainerType<ContainerFastHopper> fastHopper;
    public static ContainerType<SortingChestContainer> sortingChest;

    public static ContainerType<AddFilterContainer> addFilter;
    public static ContainerType<RootFilterContainer> rootFilter;
    public static ContainerType<NameFilterContainer> nameFilter;
    public static ContainerType<ChecklistFilterContainer> checklistFilter;
    public static ContainerType<RootFilterContainer> blockExtenderInputFilter;
    public static ContainerType<RootFilterContainer> blockExtenderOutputFilter;

    public static void register(IForgeRegistry<ContainerType<?>> registry) {
        registry.register(addFilter = register("add_filter", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity != null) {
                return new AddFilterContainer(windowId, inv, tileEntity);
            }

            throw new RuntimeException("Could not open container screen");
        }));

        registry.register(blockExtender = register("block_extender", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof TileBlockExtender) {
                return new BlockExtenderContainer(windowId, inv, (TileBlockExtender) tileEntity);
            }

            throw new RuntimeException("Could not open container screen");
        }));

        registry.register(sortingChest = register("sorting_chest", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof SortingChestTileEntity) {
                return new SortingChestContainer(windowId, inv, (SortingChestTileEntity) tileEntity);
            }

            throw new RuntimeException("Could not open container screen");
        }));

        registry.register(fastHopper = register("fast_hopper", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof FastHopperTileEntity) {
                return new ContainerFastHopper(windowId, inv, (FastHopperTileEntity) tileEntity);
            }

            throw new RuntimeException("Could not open container screen");
        }));

        registry.register(rootFilter = register("root_filter", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity != null && tileEntity.getCapability(CapabilityRootFilter.CAPABILITY).isPresent()) {
                return new RootFilterContainer(windowId, inv, tileEntity);
            }

            throw new RuntimeException("Could not open container screen");
        }));

        registry.register(nameFilter = register("name_filter", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            int filterIndex = data.readInt();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity != null) {
                Container container = inv.player.openContainer;
                if (container instanceof IRootFilterContainer) {
                    IFilter filter = ((IRootFilterContainer) container).getRootFilter().getFilter(filterIndex);
                    if (filter != null) {
                        return new NameFilterContainer(windowId, inv, tileEntity, (NameFilter) filter);
                    }
                }
            }

            throw new RuntimeException("Could not open container screen");
        }));

        registry.register(checklistFilter = register("checklist_filter", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            int filterIndex = data.readInt();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity != null) {
                Container container = inv.player.openContainer;
                if (container instanceof IRootFilterContainer) {
                    IFilter filter = ((IRootFilterContainer) container).getRootFilter().getFilter(filterIndex);
                    if (filter != null) {
                        return new ChecklistFilterContainer(windowId, inv, tileEntity, (IChecklistFilter) filter);
                    }
                }
            }

            throw new RuntimeException("Could not open container screen");
        }));

        registry.register(blockExtenderInputFilter = register("block_extender_input_filter", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof TileBlockExtender) {
                TileBlockExtender tileBlockExtender = (TileBlockExtender) tileEntity;
                return new RootFilterContainer(rootFilter, windowId, inv, tileEntity, tileBlockExtender.getInputFilter());
            }

            throw new RuntimeException("Could not open container screen");
        }));

        registry.register(blockExtenderOutputFilter = register("block_extender_output_filter", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof TileBlockExtender) {
                TileBlockExtender tileBlockExtender = (TileBlockExtender) tileEntity;
                return new RootFilterContainer(rootFilter, windowId, inv, tileEntity, tileBlockExtender.getOutputFilter());
            }

            throw new RuntimeException("Could not open container screen");
        }));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Container> ContainerType<T> register(String name, IContainerFactory<T> containerFactory) {
        return (ContainerType<T>) new ContainerType<>(containerFactory).setRegistryName(name);
    }
}
