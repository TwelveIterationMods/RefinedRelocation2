package net.blay09.mods.refinedrelocation.filter;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.client.IDrawable;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.blay09.mods.refinedrelocation.container.ChecklistFilterContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
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
    public INBT serializeNBT() {
        ListNBT list = new ListNBT();
        for (int i = 0; i < tabStates.length; i++) {
            if (tabStates[i]) {
                list.add(StringNBT.func_229705_a_(creativeTabs[i]));
            }
        }
        return list;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        ListNBT list = (ListNBT) nbt;
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
        return GuiTextures.CREATIVE_TAB_FILTER_ICON;
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

    @Nullable
    @Override
    public INamedContainerProvider getConfiguration(PlayerEntity player, TileEntity tileEntity) {
        return new INamedContainerProvider() {
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new ChecklistFilterContainer(i, playerInventory, tileEntity, CreativeTabFilter.this);
            }

            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("container.refinedrelocation:creativeTabFilter");
            }
        };
    }

    @Override
    public boolean hasConfiguration() {
        return true;
    }
}
