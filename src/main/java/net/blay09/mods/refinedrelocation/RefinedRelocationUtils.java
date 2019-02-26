package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.tile.IDroppableItemHandler;
import net.blay09.mods.refinedrelocation.util.ItemUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class RefinedRelocationUtils {

    public static void dropItemHandler(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null) {
            LazyOptional<IItemHandler> itemHandlerCap = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
            itemHandlerCap.ifPresent(itemHandler -> ItemUtils.dropItemHandlerItems(world, pos, itemHandler));
            if (tileEntity instanceof IDroppableItemHandler) {
                ((IDroppableItemHandler) tileEntity).dropItemHandlers();
            }
        }
    }

}
