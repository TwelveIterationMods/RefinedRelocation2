package net.blay09.mods.refinedrelocation.api.filter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBTBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public interface ISimpleFilter extends INBTSerializable<INBTBase> {
    boolean passes(TileEntity tilePos, ItemStack itemStack);

    @Nullable
    default IInteractionObject getConfiguration(EntityPlayer player, TileEntity tileEntity) {
        return null;
    }

    default boolean hasConfiguration() {
        return false;
    }
}
