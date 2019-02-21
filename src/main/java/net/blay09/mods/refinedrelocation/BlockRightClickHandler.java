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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
            if (tileEntity != null && tileEntity.getCapability(Capabilities.ROOT_FILTER).isPresent()) {
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

        LazyOptional<INameTaggable> capability = tileEntity.getCapability(Capabilities.NAME_TAGGABLE);
        capability.ifPresent( it -> {
            it.setCustomName(heldItem.getDisplayName().getUnformattedComponentText());
            VanillaPacketHandler.sendTileEntityUpdate(tileEntity);

            if (!player.abilities.isCreativeMode) {
                heldItem.shrink(1);
            }
        });

        return capability.isPresent();
    }

}
