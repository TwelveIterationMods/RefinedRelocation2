package net.blay09.mods.refinedrelocation.api.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;

public interface ITileGuiHandler {
    void openGui(PlayerEntity player, TileEntity tileEntity);
}
