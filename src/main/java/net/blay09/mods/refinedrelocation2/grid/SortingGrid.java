package net.blay09.mods.refinedrelocation2.grid;

import com.google.common.collect.Lists;
import net.blay09.mods.refinedrelocation2.api.RefinedRelocationAPI;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingGridMember;
import net.blay09.mods.refinedrelocation2.api.grid.ISortingGrid;

import java.util.Collection;
import java.util.List;

public class SortingGrid implements ISortingGrid {

    private final List<ISortingGridMember> memberList = Lists.newArrayList();

    public void mergeWith(ISortingGrid sortingGrid) {
        if(sortingGrid == this) {
            return;
        }
        sortingGrid.getMembers().forEach(this::addMember);
    }

    @Override
    public void addMember(ISortingGridMember member) {
        member.setSortingGrid(this);
        memberList.add(member);
    }

    @Override
    public void removeMember(ISortingGridMember member) {
        memberList.remove(member);
        for(ISortingGridMember m : memberList) {
            m.setSortingGrid(null);
        }
        memberList.forEach(RefinedRelocationAPI::addToSortingGrid);
    }

    @Override
    public Collection<ISortingGridMember> getMembers() {
        return memberList;
    }

}
