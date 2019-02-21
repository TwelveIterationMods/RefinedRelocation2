package net.blay09.mods.refinedrelocation.api.filter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBTBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISimpleFilter extends INBTSerializable<INBTBase> {
	boolean passes(TileEntity tilePos, ItemStack itemStack);
}
