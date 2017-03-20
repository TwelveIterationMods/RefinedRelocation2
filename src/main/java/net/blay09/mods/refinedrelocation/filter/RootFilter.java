package net.blay09.mods.refinedrelocation.filter;

import com.google.common.collect.Lists;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class RootFilter implements IRootFilter {

	private final List<SubFilterWrapper> filterList = Lists.newArrayList();

	@Override
	public int getFilterCount() {
		return filterList.size();
	}

	@Override
	@Nullable
	public IFilter getFilter(int index) {
		if(index < 0 || index >= filterList.size()) {
			return null;
		}
		return filterList.get(index).getFilter();
	}

	@Override
	public boolean isBlacklist(int index) {
		return !(index < 0 || index >= filterList.size()) && filterList.get(index).isBlacklist();
	}

	@Override
	public void setIsBlacklist(int index, boolean isBlacklist) {
		if(index < 0 || index >= filterList.size()) {
			return;
		}
		filterList.get(index).setBlacklist(isBlacklist);
	}

	@Override
	public void addFilter(IFilter filter) {
		filterList.add(new SubFilterWrapper(filter));
	}

	@Override
	public void removeFilter(int index) {
		filterList.remove(index);
	}

	@Override
	public boolean passes(TileEntity tileEntity, ItemStack itemStack) {
		if(itemStack.isEmpty()) {
			return false;
		}
		boolean passes = false;
		for(SubFilterWrapper filter : filterList) {
			boolean filterPasses = filter.getFilter().passes(tileEntity, itemStack);
			if(filterPasses && filter.isBlacklist()) {
				return false;
			}
			passes = passes || filterPasses;
		}
		return passes;
	}

	@Override
	public NBTBase serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagList filterList = new NBTTagList();
		for(SubFilterWrapper filter : this.filterList) {
			NBTTagCompound tagCompound = new NBTTagCompound();
			filter.writeNBT(tagCompound);
			filterList.appendTag(tagCompound);
		}
		compound.setTag("FilterList", filterList);
		return compound;
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		filterList.clear();
		NBTTagCompound compound = (NBTTagCompound) nbt;
		NBTTagList filterList = compound.getTagList("FilterList", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < filterList.tagCount(); i++) {
			NBTTagCompound tagCompound = filterList.getCompoundTagAt(i);
			SubFilterWrapper filter = SubFilterWrapper.loadFromNBT(tagCompound);
			if(filter != null) {
				this.filterList.add(filter);
			}
		}
	}

}
