package net.blay09.mods.refinedrelocation.filter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.util.GridContainer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.Map;

public class PresetFilter implements IFilter {

	private static final String ID = RefinedRelocation.MOD_ID + ":PresetFilter";

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

	public static final Preset ORES = new Preset("ORES") {
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

	public static final Preset INGOTS = new Preset("INGOTS") {
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

	public static final Preset NUGGETS = new Preset("NUGGETS") {
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

	public static final Preset GEMS = new Preset("GEMS") {
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

	public static final Preset DYES = new Preset("DYES") {
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

	public static final Preset FOOD = new Preset("FOOD") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			return itemStack.getItem() instanceof ItemFood;
		}
	};

	public static final Preset FUEL_ITEMS = new Preset("FUEL") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			return TileEntityFurnace.getItemBurnTime(itemStack) > 0;
		}
	};

	static {
		presetMap.put(ORES.getId(), ORES);
		presetMap.put(INGOTS.getId(), INGOTS);
		presetMap.put(NUGGETS.getId(), NUGGETS);
		presetMap.put(GEMS.getId(), GEMS);
		presetMap.put(DYES.getId(), DYES);
		presetMap.put(FOOD.getId(), FOOD);
		presetMap.put(FUEL_ITEMS.getId(), FUEL_ITEMS);
	}

	private final List<Preset> presets = Lists.newArrayList();

	@Override
	public String getIdentifier() {
		return ID;
	}

	@Override
	public boolean isFilterUsable(GridContainer pos) {
		return true;
	}

	@Override
	public boolean passes(GridContainer tilePos, ItemStack itemStack) {
		int[] oreIDs = OreDictionary.getOreIDs(itemStack);
		String[] oreNames = new String[oreIDs.length];
		for(int i = 0; i < oreIDs.length; i++) {
			oreNames[i] = OreDictionary.getOreName(oreIDs[i]);
		}
		for (Preset preset : presets) {
			if (preset.passes(itemStack, oreNames)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public NBTBase serializeNBT() {
		NBTTagList list = new NBTTagList();
		for(Preset preset : presets) {
			list.appendTag(new NBTTagString(preset.getId()));
		}
		return list;
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		NBTTagList list = (NBTTagList) nbt;
		for(int i = 0; i < list.tagCount(); i++) {
			presets.add(presetMap.get(list.getStringTagAt(i)));
		}
	}

}
