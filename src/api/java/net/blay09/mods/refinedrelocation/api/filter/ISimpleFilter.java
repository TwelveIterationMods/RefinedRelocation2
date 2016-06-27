package net.blay09.mods.refinedrelocation.api.filter;

import net.blay09.mods.refinedrelocation.util.GridContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISimpleFilter extends INBTSerializable<NBTBase> {
	boolean passes(GridContainer tilePos, ItemStack itemStack);
}
