package net.blay09.mods.refinedrelocation.filter;

import com.google.common.collect.Lists;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.blay09.mods.refinedrelocation.util.TileWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.List;

public class RootFilter implements IRootFilter {

	private final List<IFilter> filterList = Lists.newArrayList();

	public RootFilter() {
		filterList.add(new SameItemFilter()); // TODO test code
	}

	@Override
	public boolean passes(TileWrapper pos, ItemStack itemStack) {
		boolean passes = false;
		for(IFilter filter : filterList) {
			passes = filter.passes(pos, itemStack);
		}
		return passes;
	}

	@Override
	public NBTBase serializeNBT() {
		NBTTagList list = new NBTTagList();
		for(IFilter filter : filterList) {
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setString("Type", filter.getIdentifier());
			tagCompound.setTag("Data", filter.serializeNBT());
			list.appendTag(tagCompound);
		}
		return list;
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		NBTTagList list = (NBTTagList) nbt;
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tagCompound = list.getCompoundTagAt(i);
			IFilter filter = FilterRegistry.createFilter(tagCompound.getString("Type"));
			filter.deserializeNBT(tagCompound.getTag("Data"));
		}
	}

}
