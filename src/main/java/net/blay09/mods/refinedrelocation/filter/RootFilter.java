package net.blay09.mods.refinedrelocation.filter;

import com.google.common.collect.Lists;
import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.api.filter.IRootFilter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
		if(index < 0 || index >= filterList.size()) {
			return false;
		}
		return filterList.get(index).isBlacklist();
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
	public boolean passes(TileOrMultipart tileEntity, ItemStack itemStack) {
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
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for(SubFilterWrapper filter : filterList) {
			NBTTagCompound filterCompound = new NBTTagCompound();
			filter.writeNBT(filterCompound);
			list.appendTag(filterCompound);
		}
		tagCompound.setTag("FilterList", list);
		return tagCompound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound tagCompound) {
		filterList.clear();
		NBTTagList list = tagCompound.getTagList("FilterList", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound filterCompound = list.getCompoundTagAt(i);
			SubFilterWrapper filter = SubFilterWrapper.loadFromNBT(filterCompound);
			if(filter != null) {
				filterList.add(filter);
			}

		}
	}

}
