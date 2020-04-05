package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.tile.IDroppableItemHandler;
import net.blay09.mods.refinedrelocation.util.ItemUtils;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class RefinedRelocationUtils {

    public static LazyOptional<IRootFilter> getRootFilter(TileEntity tileEntity, int rootFilterIndex) {
        LazyOptional<IRootFilter> foundRootFilter = tileEntity.getCapability(Capabilities.MULTI_ROOT_FILTER).map(it -> it.getRootFilter(rootFilterIndex));
        if (foundRootFilter.isPresent()) {
            return foundRootFilter;
        }

        return rootFilterIndex == 0 ? tileEntity.getCapability(Capabilities.ROOT_FILTER) : LazyOptional.empty();
    }

    public static void dropItemHandler(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null) {
            if (tileEntity instanceof IDroppableItemHandler) {
                ((IDroppableItemHandler) tileEntity).getDroppedItemHandlers().forEach(itemHandler -> ItemUtils.dropItemHandlerItems(world, pos, itemHandler));
            } else {
                LazyOptional<IItemHandler> itemHandlerCap = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                itemHandlerCap.ifPresent(itemHandler -> ItemUtils.dropItemHandlerItems(world, pos, itemHandler));
            }
        }
    }

    /**
     * Stupid LazyOptional doesn't allow nullability at all, keeping you locked in optional hell.
     * Helper function to avoid IDE warnings caused by this, since I'm not willing to rewrite my code just for this.
     */
    @Nullable
    @SuppressWarnings("ConstantConditions")
    public static <T> T orNull(LazyOptional<T> optional) {
        return optional.orElse(null);
    }

    public static int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null) {
            LazyOptional<IItemHandler> itemHandlerCap = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
            return itemHandlerCap.map(ItemHandlerHelper::calcRedstoneFromInventory).orElse(0);
        }

        return 0;
    }
}
