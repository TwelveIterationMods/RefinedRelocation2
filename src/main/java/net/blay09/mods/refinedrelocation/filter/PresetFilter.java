package net.blay09.mods.refinedrelocation.filter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.blay09.mods.refinedrelocation.api.client.IFilterIcon;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.client.ClientProxy;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.Map;

public class PresetFilter implements IChecklistFilter {

	public static final String ID = RefinedRelocation.MOD_ID + ":PresetFilter";

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

	public static final Preset ORES = new Preset("Ores") {
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

	public static final Preset INGOTS = new Preset("Ingots") {
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

	public static final Preset NUGGETS = new Preset("Nuggets") {
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

	public static final Preset GEMS = new Preset("Gems") {
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

	public static final Preset DYES = new Preset("Dyes") {
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

	public static final Preset FOOD = new Preset("Food") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			return itemStack.getItem() instanceof ItemFood;
		}
	};

	public static final Preset FUEL_ITEMS = new Preset("Fuel") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			return TileEntityFurnace.getItemBurnTime(itemStack) > 0;
		}
	};

	public static final Preset BLOCKS = new Preset("Blocks") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			return itemStack.getItem() instanceof ItemBlock;
		}
	};

	public static final Preset UNSTACKABLE = new Preset("Unstackable") {
		@Override
		public boolean passes(ItemStack itemStack, String[] oreNames) {
			return itemStack.getMaxStackSize() <= 1;
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
	public boolean isFilterUsable(TileOrMultipart tileEntity) {
		return true;
	}

	@Override
	public boolean passes(TileOrMultipart tileEntity, ItemStack itemStack) {
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
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < presetStates.length; i++) {
			if(presetStates[i]) {
				list.appendTag(new NBTTagString(presetList.get(i).getId()));
			}
		}
		tagCompound.setTag("Presets", list);
		return tagCompound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound tagCompound) {
		NBTTagList list = tagCompound.getTagList("Presets", Constants.NBT.TAG_STRING);
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
		return "filter.refinedrelocation:PresetFilter";
	}

	@Override
	public String getDescriptionLangKey() {
		return "filter.refinedrelocation:PresetFilter.description";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IFilterIcon getFilterIcon() {
		return ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:icon_PresetFilter");
	}

	@Override
	public String getOptionLangKey(int option) {
		return "filter.refinedrelocation:PresetFilter.option" + presetList.get(option).getId();
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

}
