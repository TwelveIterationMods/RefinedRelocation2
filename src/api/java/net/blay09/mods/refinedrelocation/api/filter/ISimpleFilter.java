package net.blay09.mods.refinedrelocation.api.filter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISimpleFilter extends INBTSerializable<NBTBase> {
	boolean passes(TileEntity tilePos, ItemStack itemStack);
}
