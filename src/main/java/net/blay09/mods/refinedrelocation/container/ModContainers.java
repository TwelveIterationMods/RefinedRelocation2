package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.blay09.mods.refinedrelocation.filter.NameFilter;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.blay09.mods.refinedrelocation.tile.TileFastHopper;
import net.blay09.mods.refinedrelocation.tile.SortingChestTileEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.IForgeRegistry;

public class ModContainers {
    public static ContainerType<ContainerBlockExtender> blockExtender;
    public static ContainerType<ContainerFastHopper> fastHopper;
    public static ContainerType<ContainerSortingChest> sortingChest;

    public static ContainerType<RootFilterContainer> rootFilter;
    public static ContainerType<NameFilterContainer> nameFilter;
    public static ContainerType<ChecklistFilterContainer> checklistFilter;
    public static ContainerType<RootFilterContainer> blockExtenderRootFilter;

    public static void register(IForgeRegistry<ContainerType<?>> registry) {
        registry.register(blockExtender = register("block_extender", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            int clickedFaceIndex = data.readByte();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof TileBlockExtender) {
                Direction clickedFace = Direction.byIndex(clickedFaceIndex);
                ContainerBlockExtender container = new ContainerBlockExtender(windowId, inv, (TileBlockExtender) tileEntity);
                container.setClickedFace(clickedFace);
                return container;
            }

            return null;
        }));

        registry.register(sortingChest = register("sorting_chest", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof SortingChestTileEntity) {
                return new ContainerSortingChest(windowId, inv, (SortingChestTileEntity) tileEntity);
            }

            return null;
        }));

        registry.register(fastHopper = register("fast_hopper", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof TileFastHopper) {
                return new ContainerFastHopper(windowId, inv, (TileFastHopper) tileEntity);
            }

            return null;
        }));

        registry.register(rootFilter = register("root_filter", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity != null && tileEntity.getCapability(CapabilityRootFilter.CAPABILITY).isPresent()) {
                return new RootFilterContainer(windowId, inv, tileEntity);
            }

            return null;
        }));

        registry.register(nameFilter = register("name_filter", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            int filterIndex = data.readInt();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity != null) {
                Container container = inv.player.openContainer;
                if (container instanceof RootFilterContainer) {
                    IFilter filter = ((RootFilterContainer) container).getRootFilter().getFilter(filterIndex);
                    if (filter != null) {
                        return new NameFilterContainer(windowId, inv, tileEntity, (NameFilter) filter);
                    }
                }
            }

            return null;
        }));

        registry.register(checklistFilter = register("checklist_filter", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            int filterIndex = data.readInt();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity != null) {
                Container container = inv.player.openContainer;
                if (container instanceof RootFilterContainer) {
                    IFilter filter = ((RootFilterContainer) container).getRootFilter().getFilter(filterIndex);
                    if (filter != null) {
                        return new ChecklistFilterContainer(windowId, inv, tileEntity, (IChecklistFilter) filter);
                    }
                }
            }

            return null;
        }));

        registry.register(blockExtenderRootFilter = register("block_extender_root_filter", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            boolean isOutputFilter = data.readInt() == 1;

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof TileBlockExtender) {
                TileBlockExtender tileBlockExtender = (TileBlockExtender) tileEntity;
                return new RootFilterContainer(windowId, inv, tileEntity, isOutputFilter ? tileBlockExtender.getOutputFilter() : tileBlockExtender.getInputFilter());
            }

            return null;
        }));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Container> ContainerType<T> register(String name, IContainerFactory<T> containerFactory) {
        return (ContainerType<T>) new ContainerType<>(containerFactory).setRegistryName(name);
    }
}
