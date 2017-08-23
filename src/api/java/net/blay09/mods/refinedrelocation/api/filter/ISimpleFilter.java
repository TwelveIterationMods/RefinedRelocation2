package net.blay09.mods.refinedrelocation.api.filter;

import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISimpleFilter extends INBTSerializable<NBTTagCompound> {
	boolean passes(TileOrMultipart tilePos, ItemStack itemStack);
}
