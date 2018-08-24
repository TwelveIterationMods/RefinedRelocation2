package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.INameTaggable;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation.network.VanillaPacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockRightClickHandler {

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        EntityPlayer player = event.getEntityPlayer();
        if (tryNameTile(player, world, pos, event.getItemStack())) {
            event.setCancellationResult(EnumActionResult.SUCCESS);
            event.setCanceled(true);
            return;
        }


        if (!world.isRemote && player.isSneaking()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity != null && tileEntity.hasCapability(Capabilities.ROOT_FILTER, null)) {
                RefinedRelocationAPI.openRootFilterGui(player, tileEntity);
                event.setCancellationResult(EnumActionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }

    private static boolean tryNameTile(EntityPlayer player, World world, BlockPos pos, ItemStack heldItem) {
        if (heldItem.isEmpty() || heldItem.getItem() != Items.NAME_TAG) {
            return false;
        }

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity == null) {
            return false;
        }

        INameTaggable nameTaggable = tileEntity.getCapability(Capabilities.NAME_TAGGABLE, null);
        if (nameTaggable == null && tileEntity instanceof INameTaggable) {
            // For backwards compatibility, check for the interface as well
            nameTaggable = (INameTaggable) tileEntity;
        }

        if (nameTaggable != null) {
            nameTaggable.setCustomName(heldItem.getDisplayName());
            VanillaPacketHandler.sendTileEntityUpdate(tileEntity);

            if (!player.capabilities.isCreativeMode) {
                heldItem.shrink(1);
            }

            return true;
        }

        return false;
    }

}
