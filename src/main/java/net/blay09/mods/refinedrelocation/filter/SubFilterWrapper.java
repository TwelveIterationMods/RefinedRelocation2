package net.blay09.mods.refinedrelocation.filter;

import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public class SubFilterWrapper {

	private final IFilter filter;
	private boolean isBlacklist;

	public SubFilterWrapper(IFilter filter) {
		this.filter = filter;
	}

	public boolean isBlacklist() {
		return isBlacklist;
	}

	public void setBlacklist(boolean blacklist) {
		isBlacklist = blacklist;
	}

	public IFilter getFilter() {
		return filter;
	}

	public NBTTagCompound writeNBT(NBTTagCompound compound) {
		compound.setString("Type", filter.getIdentifier());
		compound.setTag("Data", filter.serializeNBT());
		compound.setBoolean("IsBlacklist", isBlacklist);
		return compound;
	}

	@Nullable
	public static SubFilterWrapper loadFromNBT(NBTTagCompound tagCompound) {
		IFilter filter = FilterRegistry.createFilter(tagCompound.getString("Type"));
		if(filter != null) {
			filter.deserializeNBT(tagCompound.getTag("Data"));
			SubFilterWrapper wrapper = new SubFilterWrapper(filter);
			wrapper.isBlacklist = tagCompound.getBoolean("IsBlacklist");
			return wrapper;
		}
		return null;
	}
}
