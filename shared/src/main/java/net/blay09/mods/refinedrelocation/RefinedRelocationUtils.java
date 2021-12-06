package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.block.entity.IDroppableItemHandler;
import net.blay09.mods.refinedrelocation.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Optional;

public class RefinedRelocationUtils {

    public static Optional<IRootFilter> getRootFilter(BlockEntity tileEntity, int rootFilterIndex) {
        Optional<IRootFilter> foundRootFilter = tileEntity.getCapability(Capabilities.MULTI_ROOT_FILTER).map(it -> it.getRootFilter(rootFilterIndex));
        if (foundRootFilter.isPresent()) {
            return foundRootFilter;
        }

        return rootFilterIndex == 0 ? tileEntity.getCapability(Capabilities.ROOT_FILTER).resolve() : Optional.empty();
    }

    public static void dropItemHandler(Level level, BlockPos pos) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity != null) {
            if (tileEntity instanceof IDroppableItemHandler) {
                ((IDroppableItemHandler) tileEntity).getDroppedItemHandlers().forEach(itemHandler -> ItemUtils.dropItemHandlerItems(level, pos, itemHandler));
            } else {
                LazyOptional<IItemHandler> itemHandlerCap = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                itemHandlerCap.ifPresent(itemHandler -> ItemUtils.dropItemHandlerItems(level, pos, itemHandler));
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

    public static int getComparatorInputOverride(BlockState state, Level level, BlockPos pos) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity != null) {
            LazyOptional<IItemHandler> itemHandlerCap = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
            return itemHandlerCap.map(ItemHandlerHelper::calcRedstoneFromInventory).orElse(0);
        }

        return 0;
    }
}
