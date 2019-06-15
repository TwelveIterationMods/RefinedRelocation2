package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RefinedRelocation.MOD_ID)
public class BlockRightClickHandler {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        PlayerEntity player = event.getEntityPlayer();
        if (!world.isRemote && player.isSneaking()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity != null && tileEntity.getCapability(Capabilities.ROOT_FILTER).isPresent()) {
                RefinedRelocationAPI.openRootFilterGui(player, tileEntity);
                event.setCancellationResult(ActionResultType.SUCCESS);
                event.setCanceled(true);
            }
        }
    }

}
