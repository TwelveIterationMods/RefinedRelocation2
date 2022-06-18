package net.blay09.mods.refinedrelocation.filter;

import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.client.IDrawable;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.blay09.mods.refinedrelocation.menu.ChecklistFilterMenu;
import net.blay09.mods.refinedrelocation.mixin.CreativeModeTabAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

public class CreativeTabFilter implements IChecklistFilter {

    public static final String ID = RefinedRelocation.MOD_ID + ":creative_tab_filter";

    // These are the Inventory, Search and Hotbar tabs that we don't care about
    private static final int IGNORED_TABS = 3;

    public static String[] creativeTabs = new String[0];

    public static void gatherCreativeTabs() {
        creativeTabs = new String[CreativeModeTab.TABS.length - IGNORED_TABS];
        int i = 0;
        for (int j = 0; j < CreativeModeTab.TABS.length; j++) {
            if (CreativeModeTab.TABS[j] == CreativeModeTab.TAB_INVENTORY
                    || CreativeModeTab.TABS[j] == CreativeModeTab.TAB_SEARCH
                    || CreativeModeTab.TABS[j] == CreativeModeTab.TAB_HOTBAR) {
                continue;
            }
            creativeTabs[i] = ((CreativeModeTabAccessor) CreativeModeTab.TABS[j]).getLangId();
            i++;
        }
    }

    private final boolean[] tabStates = new boolean[creativeTabs.length];

    @Override
    public String getIdentifier() {
        return ID;
    }

    @Override
    public boolean isFilterUsable(BlockEntity blockEntity) {
        return true;
    }

    @Override
    public boolean passes(BlockEntity blockEntity, ItemStack itemStack, ItemStack originalStack) {
        CreativeModeTab itemTab = itemStack.getItem().getItemCategory();
        if (itemTab == null) {
            return false;
        }

        int shiftedTabIndex = itemTab.getId();
        if (itemTab.getId() >= CreativeModeTab.TAB_SEARCH.getId()) {
            shiftedTabIndex--;
        }
        if (itemTab.getId() >= CreativeModeTab.TAB_INVENTORY.getId()) {
            shiftedTabIndex--;
        }
        if (itemTab.getId() >= CreativeModeTab.TAB_HOTBAR.getId()) {
            shiftedTabIndex--;
        }

        return tabStates[shiftedTabIndex];
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (int i = 0; i < tabStates.length; i++) {
            if (tabStates[i]) {
                list.add(StringTag.valueOf(creativeTabs[i]));
            }
        }
        tag.put("Tabs", list);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        ListTag list = tag.getList("Tabs", Tag.TAG_STRING);
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
    public MenuProvider getConfiguration(Player player, BlockEntity blockEntity, int rootFilterIndex, int filterIndex) {
        return new BalmMenuProvider() {
            @Override
            public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
                return new ChecklistFilterMenu(i, playerInventory, blockEntity, rootFilterIndex, CreativeTabFilter.this);
            }

            @Override
            public Component getDisplayName() {
                return Component.translatable("container.refinedrelocation:creative_tab_filter");
            }

            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                buf.writeBlockPos(blockEntity.getBlockPos());
                buf.writeByte(rootFilterIndex);
                buf.writeByte(filterIndex);
            }
        };
    }

    @Override
    public boolean hasConfiguration() {
        return true;
    }
}
