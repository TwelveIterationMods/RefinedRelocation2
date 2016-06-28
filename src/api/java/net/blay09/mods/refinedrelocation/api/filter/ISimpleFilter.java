package net.blay09.mods.refinedrelocation.api.filter;

import net.blay09.mods.refinedrelocation.util.TileWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISimpleFilter extends INBTSerializable<NBTBase> {
	boolean passes(TileWrapper tilePos, ItemStack itemStack);
}
