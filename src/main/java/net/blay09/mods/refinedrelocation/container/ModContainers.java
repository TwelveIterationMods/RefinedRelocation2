package net.blay09.mods.refinedrelocation.container;

import net.blay09.mods.refinedrelocation.capability.CapabilityRootFilter;
import net.blay09.mods.refinedrelocation.tile.TileBlockExtender;
import net.blay09.mods.refinedrelocation.tile.TileFastHopper;
import net.blay09.mods.refinedrelocation.tile.TileSortingChest;
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

    public static ContainerType<ContainerChecklistFilter> checkListFilter;
    public static ContainerType<ContainerNameFilter> nameFilter;
    public static ContainerType<ContainerRootFilter> rootFilter;

    public static void register(IForgeRegistry<ContainerType<?>> registry) {
        registry.register(blockExtender = register("block_extender", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            int clickedFaceIndex = data.readByte();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof TileBlockExtender) {
                Direction clickedFace = Direction.byIndex(clickedFaceIndex);
                return new ContainerBlockExtender(windowId, inv, (TileBlockExtender) tileEntity, clickedFace);
            }

            return null;
        }));

        registry.register(sortingChest = register("sorting_chest", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof TileSortingChest) {
                return new ContainerSortingChest(windowId, inv, (TileSortingChest) tileEntity);
            }

            return null;
        }));

        registry.register(register("fast_hopper", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof TileFastHopper) {
                return new ContainerFastHopper(windowId, inv, (TileFastHopper) tileEntity);
            }

            return null;
        }));

        registry.register(register("root_filter", (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity != null && tileEntity.getCapability(CapabilityRootFilter.CAPABILITY).isPresent()) {
                return new ContainerRootFilter(windowId, inv, tileEntity);
            }

            return null;
        }));

        registry.register(new ContainerType<>((IContainerFactory<ContainerRootFilter>) (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity != null) {
                // TODO NYI
                /*Container container = inv.player.openContainer;
                if (container instanceof ContainerRootFilter) {
                    IFilter filter = ((ContainerRootFilter) container).getRootFilter().getFilter(it.getAdditionalData().readInt());
                    if (filter instanceof IChecklistFilter) {
                        return new ChecklistFilterScreen(player, tileEntity, (IChecklistFilter) filter);
                    } else if (filter instanceof NameFilter) {
                        return new NameFilterScreen(player, tileEntity, (NameFilter) filter);
                    }
                }
                return new ContainerRootFilter(windowId, inv, tileEntity);*/
            }

            return null;
        }).setRegistryName("any_filter"));

        registry.register(new ContainerType<>((IContainerFactory<ContainerRootFilter>) (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            boolean isOutputFilter = data.readInt() == 1;

            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof TileBlockExtender) {
                TileBlockExtender tileBlockExtender = (TileBlockExtender) tileEntity;
                return new ContainerRootFilter(windowId, inv, tileEntity, isOutputFilter ? tileBlockExtender.getOutputFilter() : tileBlockExtender.getInputFilter());
            }

            return null;
        }).setRegistryName("block_extender_root_filter"));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Container> ContainerType<T> register(String name, IContainerFactory<T> containerFactory) {
        return (ContainerType<T>) new ContainerType<>(containerFactory).setRegistryName(name);
    }
}
