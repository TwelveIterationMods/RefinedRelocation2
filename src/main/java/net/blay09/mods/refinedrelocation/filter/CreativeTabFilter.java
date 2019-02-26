package net.blay09.mods.refinedrelocation.filter;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.client.IDrawable;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collection;

public class CreativeTabFilter implements IChecklistFilter {

    public static final String ID = RefinedRelocation.MOD_ID + ":creative_tab_filter";

    // These are the Inventory, Search and Hotbar tabs that we don't care about
    private static final int IGNORED_TABS = 3;

    public static String[] creativeTabs = new String[0];

    public static void gatherCreativeTabs() {
        creativeTabs = new String[ItemGroup.GROUPS.length - IGNORED_TABS];
        int i = 0;
        for (int j = 0; j < ItemGroup.GROUPS.length; j++) {
            if (ItemGroup.GROUPS[j] == ItemGroup.INVENTORY
                    || ItemGroup.GROUPS[j] == ItemGroup.SEARCH
                    || ItemGroup.GROUPS[j] == ItemGroup.HOTBAR) {
                continue;
            }
            creativeTabs[i] = ItemGroup.GROUPS[j].tabLabel; // getTabLabel is client-only
            i++;
        }
    }

    private final boolean[] tabStates = new boolean[creativeTabs.length];

    @Override
    public String getIdentifier() {
        return ID;
    }

    @Override
    public boolean isFilterUsable(TileEntity tileEntity) {
        return true;
    }

    @Override
    public boolean passes(TileEntity tileEntity, ItemStack itemStack) {
        Collection<ItemGroup> itemTabs = itemStack.getItem().getCreativeTabs();
        for (ItemGroup itemTab : itemTabs) {
            if (itemTab == null) {
                continue;
            }
            // getTabIndex is client-only
            int shiftedTabIndex = itemTab.index;
            if (itemTab.index >= ItemGroup.SEARCH.index) {
                shiftedTabIndex--;
            }
            if (itemTab.index >= ItemGroup.INVENTORY.index) {
                shiftedTabIndex--;
            }
            if (itemTab.index >= ItemGroup.HOTBAR.index) {
                shiftedTabIndex--;
            }
            if (tabStates[shiftedTabIndex]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public INBTBase serializeNBT() {
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < tabStates.length; i++) {
            if (tabStates[i]) {
                list.add(new NBTTagString(creativeTabs[i]));
            }
        }
        return list;
    }

    @Override
    public void deserializeNBT(INBTBase nbt) {
        NBTTagList list = (NBTTagList) nbt;
        for (int i = 0; i < list.size(); i++) {
            String tabLabel = list.getString(i);
            for (int j = 0; j < creativeTabs.length; j++) {
                if (creativeTabs[j].equals(tabLabel)) {
                    tabStates[j] = true;
                }
            }
        }
    }

    @Override
    public String getLangKey() {
        return "filter.refinedrelocation:creative_tab_filter";
    }

    @Override
    public String getDescriptionLangKey() {
        return "filter.refinedrelocation:creative_tab_filter.description";
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public IDrawable getFilterIcon() {
        return ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:icon_creative_tab_filter");
    }

    @Override
    public String getOptionLangKey(int option) {
        return "itemGroup." + creativeTabs[option];
    }

    @Override
    public void setOptionChecked(int option, boolean checked) {
        tabStates[option] = checked;
    }

    @Override
    public boolean isOptionChecked(int option) {
        return tabStates[option];
    }

    @Override
    public int getOptionCount() {
        return creativeTabs.length;
    }

    @Override
    public int getVisualOrder() {
        return 700;
    }
}
