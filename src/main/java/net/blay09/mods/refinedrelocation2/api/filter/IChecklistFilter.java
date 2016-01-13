package net.blay09.mods.refinedrelocation2.api.filter;

public interface IChecklistFilter extends IFilter {

    String getName(int optionIndex);
    void setValue(int optionIndex, boolean value);
    boolean getValue(int optionIndex);
    int getOptionCount();

}
