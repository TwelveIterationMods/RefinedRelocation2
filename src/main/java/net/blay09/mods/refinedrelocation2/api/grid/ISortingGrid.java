package net.blay09.mods.refinedrelocation2.api.grid;

import net.blay09.mods.refinedrelocation2.api.capability.ISortingGridMember;

import java.util.Collection;

public interface ISortingGrid {
    Collection<ISortingGridMember> getMembers();
    void addMember(ISortingGridMember member);
    void removeMember(ISortingGridMember member);
}
