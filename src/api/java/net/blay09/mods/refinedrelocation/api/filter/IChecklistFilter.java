package net.blay09.mods.refinedrelocation.api.filter;

public interface IChecklistFilter extends IFilter {
	String getOptionName(int option);
	void setOptionChecked(int option, boolean checked);
	boolean isOptionChecked(int option);
	int getOptionCount();
}
