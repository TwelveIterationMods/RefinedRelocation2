package net.blay09.mods.refinedrelocation.api.filter;

public interface IChecklistFilter extends IFilter {
	String getName(int option);
	void setChecked(int option, boolean checked);
	boolean isChecked(int option);
	int getOptionCount();
}
