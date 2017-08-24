package net.blay09.mods.refinedrelocation.filter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.client.IFilterIcon;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.client.ClientProxy;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.Map;

public class PresetFilter implements IChecklistFilter {

	public static final String ID = RefinedRelocation.MOD_ID + ":preset_filter";

	public abstract static class Preset {
		private final String id;

		public Preset(String id) {
			this.id = id;
		}

		public final String getId() {
			return id;
		}

		public abstract boolean passes(ItemStack itemStack, String[] oreNames);
	}

	private static final Map<String, Preset> presetMap = Maps.newHashMap();
	private static final List<Preset> presetList = Lists.newArrayList();

	public static final Preset ORES = new Preset("ores") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			for(String oreName : oreNames) {
				if(oreName.startsWith("ore")) {
					return true;
				}
			}
			return false;
		}
	};

	public static final Preset INGOTS = new Preset("ingots") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			for(String oreName : oreNames) {
				if(oreName.startsWith("ingot")) {
					return true;
				}
			}
			return false;
		}
	};

	public static final Preset NUGGETS = new Preset("nuggets") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			for(String oreName : oreNames) {
				if(oreName.startsWith("nugget")) {
					return true;
				}
			}
			return false;
		}
	};

	public static final Preset GEMS = new Preset("gems") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			for(String oreName : oreNames) {
				if(oreName.startsWith("gem")) {
					return true;
				}
			}
			return false;
		}
	};

	public static final Preset DYES = new Preset("dyes") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			for(String oreName : oreNames) {
				if(oreName.startsWith("dye")) {
					return true;
				}
			}
			return false;
		}
	};

	public static final Preset FOOD = new Preset("food") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			return itemStack.getItem() instanceof ItemFood;
		}
	};

	public static final Preset FUEL_ITEMS = new Preset("fuel") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			return TileEntityFurnace.getItemBurnTime(itemStack) > 0;
		}
	};

	public static final Preset BLOCKS = new Preset("blocks") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			return itemStack.getItem() instanceof ItemBlock;
		}
	};

	public static final Preset UNSTACKABLE = new Preset("unstackable") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			return itemStack.getMaxStackSize() <= 1;
		}
	};

	public static final Preset REPAIRABLE = new Preset("repairable") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			return itemStack.getItem().isRepairable();
		}
	};

	public static final Preset WEAPONS = new Preset("weapons") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			return itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemBow;
		}
	};

	public static final Preset TOOLS = new Preset("tools") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			return itemStack.getItem() == Items.CARROT_ON_A_STICK || itemStack.getItem() == Items.FLINT_AND_STEEL || itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemFishingRod || itemStack.getItem() instanceof ItemShears;
		}
	};

	public static final Preset ENCHANTED = new Preset("enchanted") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			return itemStack.isItemEnchanted();
		}
	};

	static {
		registerPreset(BLOCKS);
		registerPreset(ORES);
		registerPreset(INGOTS);
		registerPreset(NUGGETS);
		registerPreset(GEMS);
		registerPreset(FOOD);
		registerPreset(DYES);
		registerPreset(UNSTACKABLE);
		registerPreset(FUEL_ITEMS);
		registerPreset(REPAIRABLE);
		registerPreset(WEAPONS);
		registerPreset(TOOLS);
		registerPreset(ENCHANTED);

		presetList.addAll(presetMap.values());
	}

	private static void registerPreset(Preset preset) {
		presetMap.put(preset.getId(), preset);
		presetList.add(preset);
	}

	private final boolean[] presetStates = new boolean[presetList.size()];

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
		int[] oreIDs = OreDictionary.getOreIDs(itemStack);
		String[] oreNames = new String[oreIDs.length];
		for(int i = 0; i < oreIDs.length; i++) {
			oreNames[i] = OreDictionary.getOreName(oreIDs[i]);
		}
		for (int i = 0; i < presetList.size(); i++) {
			if (presetStates[i] && presetList.get(i).passes(itemStack, oreNames)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public NBTBase serializeNBT() {
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < presetStates.length; i++) {
			if(presetStates[i]) {
				list.appendTag(new NBTTagString(presetList.get(i).getId()));
			}
		}
		return list;
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		NBTTagList list = (NBTTagList) nbt;
		for(int i = 0; i < list.tagCount(); i++) {
			Preset preset = presetMap.get(list.getStringTagAt(i));
			if(preset != null) {
				int index = presetList.indexOf(preset);
				if(index != -1) {
					presetStates[index] = true;
				}
			}
		}
	}

	@Override
	public String getLangKey() {
		return "filter.refinedrelocation:preset_filter";
	}

	@Override
	public String getDescriptionLangKey() {
		return "filter.refinedrelocation:preset_filter.description";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IFilterIcon getFilterIcon() {
		return ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:icon_preset_filter");
	}

	@Override
	public String getOptionLangKey(int option) {
		return "filter.refinedrelocation:preset_filter.option_" + presetList.get(option).getId();
	}

	@Override
	public void setOptionChecked(int option, boolean checked) {
		presetStates[option] = checked;
	}

	@Override
	public boolean isOptionChecked(int option) {
		return presetStates[option];
	}

	@Override
	public int getOptionCount() {
		return presetMap.size();
	}

	@Override
	public int getVisualOrder() {
		return 1000;
	}
}
