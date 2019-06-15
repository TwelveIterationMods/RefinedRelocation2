package net.blay09.mods.refinedrelocation.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ServerWorld;

public class VanillaPacketHandler {

    public static void sendTileEntityUpdate(TileEntity tileEntity) {
        if (tileEntity.getWorld().isRemote) {
            return;
        }

        SUpdateTileEntityPacket updatePacket = null;
        ServerWorld worldServer = (ServerWorld) tileEntity.getWorld();
        int chunkX = tileEntity.getPos().getX() >> 4;
        int chunkZ = tileEntity.getPos().getZ() >> 4;
        for (PlayerEntity entityPlayer : tileEntity.getWorld().getPlayers()) {
            ServerPlayerEntity entityPlayerMP = (ServerPlayerEntity) entityPlayer;
            if (worldServer.getChunkProvider().isPlayerWatchingChunk(entityPlayerMP, chunkX, chunkZ)) {
                if (updatePacket == null) {
                    updatePacket = tileEntity.getUpdatePacket();
                    if (updatePacket == null) {
                        return;
                    }
                }

                entityPlayerMP.connection.sendPacket(updatePacket);
            }
        }
    }

}
