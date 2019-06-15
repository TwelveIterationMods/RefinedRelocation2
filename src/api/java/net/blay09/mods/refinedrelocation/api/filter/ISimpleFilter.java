package net.blay09.mods.refinedrelocation.api.filter;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public interface ISimpleFilter extends INBTSerializable<INBT> {
    boolean passes(TileEntity tilePos, ItemStack itemStack);

    @Nullable
    default INamedContainerProvider getConfiguration(PlayerEntity player, TileEntity tileEntity) {
        return null;
    }

    default boolean hasConfiguration() {
        return false;
    }
}
