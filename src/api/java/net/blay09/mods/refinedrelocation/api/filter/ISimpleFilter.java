package net.blay09.mods.refinedrelocation.api.filter;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public interface ISimpleFilter extends INBTSerializable<INBT> {
    @Deprecated
    default boolean passes(TileEntity tileEntity, ItemStack itemStack) {
        return passes(tileEntity, itemStack, itemStack);
    }

    boolean passes(TileEntity tileEntity, ItemStack itemStack, ItemStack originalStack);

    @Nullable
    default INamedContainerProvider getConfiguration(PlayerEntity player, TileEntity tileEntity, int rootFilterIndex) {
        return null;
    }

    default boolean hasConfiguration() {
        return false;
    }
}
