package net.blay09.mods.refinedrelocation.filter;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.blay09.mods.refinedrelocation.api.client.IFilterIcon;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.client.ClientProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabFilter implements IChecklistFilter {

	public static final String ID = RefinedRelocation.MOD_ID + ":CreativeTabFilter";

	public static String[] creativeTabs = new String[0];

	public static void gatherCreativeTabs() {
		creativeTabs = new String[CreativeTabs.CREATIVE_TAB_ARRAY.length - 2];
		int i = 0;
		for(int j = 0; j < CreativeTabs.CREATIVE_TAB_ARRAY.length; j++) {
			if(CreativeTabs.CREATIVE_TAB_ARRAY[j] == CreativeTabs.INVENTORY || CreativeTabs.CREATIVE_TAB_ARRAY[j] == CreativeTabs.SEARCH) {
				continue;
			}
			creativeTabs[i] = CreativeTabs.CREATIVE_TAB_ARRAY[j].tabLabel;
			i++;
		}
	}

	private final boolean[] tabStates = new boolean[creativeTabs.length];

	@Override
	public String getIdentifier() {
		return ID;
	}

	@Override
	public boolean isFilterUsable(TileOrMultipart tileEntity) {
		return true;
	}

	@Override
	public boolean passes(TileOrMultipart tileEntity, ItemStack itemStack) {
		// NOTE We can't use getCreativeTabs because it calls a client-only getCreativeTab() function - thanks Notch!
//		CreativeTabs[] itemTabs = itemStack.getItem().getCreativeTabs();
//		for (CreativeTabs itemTab : itemTabs) {
		CreativeTabs itemTab = itemStack.getItem().tabToDisplayOn;
		if (itemTab == null) {
			return false;
		}
		int shiftedTabIndex = itemTab.tabIndex;
		if(itemTab.tabIndex >= CreativeTabs.SEARCH.tabIndex) {
			shiftedTabIndex--;
		}
		if(itemTab.tabIndex >= CreativeTabs.INVENTORY.tabIndex) {
			shiftedTabIndex--;
		}
		if (tabStates[shiftedTabIndex]) {
			return true;
		}
//		}
		return false;
	}

	@Override
	public NBTBase serializeNBT() {
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < tabStates.length; i++) {
			if(tabStates[i]) {
				list.appendTag(new NBTTagString(creativeTabs[i]));
			}
		}
		return list;
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		NBTTagList list = (NBTTagList) nbt;
		for(int i = 0; i < list.tagCount(); i++) {
			String tabLabel = list.getStringTagAt(i);
			for(int j = 0; j < creativeTabs.length; j++) {
				if(creativeTabs[j].equals(tabLabel)) {
					tabStates[j] = true;
				}
			}
		}
	}

	@Override
	public String getLangKey() {
		return "filter.refinedrelocation:CreativeTabFilter";
	}

	@Override
	public String getDescriptionLangKey() {
		return "filter.refinedrelocation:CreativeTabFilter.description";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IFilterIcon getFilterIcon() {
		return ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:icon_CreativeTabFilter");
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

}
