package net.blay09.mods.refinedrelocation.filter;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.blay09.mods.refinedrelocation.api.client.IFilterIcon;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.client.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

public class ModFilter implements IChecklistFilter {

	public static final String ID = RefinedRelocation.MOD_ID + ":ModFilter";

	private static class ModWithName {
		private final int index;
		private final String id;
		private final String name;

		public ModWithName(int index, String id, String name) {
			this.index = index;
			this.id = id;
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

	private static final Map<String, ModWithName> modList = Maps.newHashMap();
	public static String[] modIds = new String[0];

	public static void gatherMods() {
		Set<String> modSet = Sets.newHashSet();
		for(ResourceLocation registryName : Block.REGISTRY.getKeys()) {
			modSet.add(registryName.getResourceDomain());
		}
		String[] unsorted = modSet.toArray(new String[modSet.size()]);
		Arrays.sort(unsorted, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if(o1.equals("minecraft")) {
					return -1;
				} else if(o2.equals("minecraft")) {
					return 1;
				}
				return o2.compareTo(o1);
			}
		});
		setModList(unsorted);
	}

	public static void setModList(String[] modIDs) {
		ModFilter.modIds = modIDs;
		Map<String, ModContainer> mods = Loader.instance().getIndexedModList();
		modList.clear();
		for(int i = 0; i < modIDs.length; i++) {
			if(modIDs[i].equals("minecraft")) {
				modList.put(modIDs[i], new ModWithName(i, modIDs[i], "Minecraft"));
			} else {
				ModContainer modContainer = mods.get(modIDs[i]);
				modList.put(modIDs[i], new ModWithName(i, modIDs[i], modContainer != null ? modContainer.getName() : modIDs[i]));
			}
		}
	}

	private final boolean[] modStates = new boolean[modIds.length];

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
		ModWithName modWithName = modList.get(itemStack.getItem().getRegistryName().getResourceDomain());
		return modWithName != null && modStates[modWithName.getIndex()];
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < modStates.length; i++) {
			if(modStates[i]) {
				list.appendTag(new NBTTagString(modIds[i]));
			}
		}
		tagCompound.setTag("Mods", list);
		return tagCompound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound tagCompound) {
		NBTTagList list = tagCompound.getTagList("Mods", Constants.NBT.TAG_STRING);
		for(int i = 0; i < list.tagCount(); i++) {
			String modId = list.getStringTagAt(i);
			for(int j = 0; j < modIds.length; j++) {
				if(modIds[j].equals(modId)) {
					modStates[j] = true;
				}
			}
		}
	}

	@Override
	public String getLangKey() {
		return "filter.refinedrelocation:ModFilter";
	}

	@Override
	public String getDescriptionLangKey() {
		return "filter.refinedrelocation:ModFilter.description";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IFilterIcon getFilterIcon() {
		return ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:icon_ModFilter");
	}

	@Override
	public String getOptionLangKey(int option) {
		return modList.get(modIds[option]).getName();
	}

	@Override
	public void setOptionChecked(int option, boolean checked) {
		modStates[option] = checked;
	}

	@Override
	public boolean isOptionChecked(int option) {
		return modStates[option];
	}

	@Override
	public int getOptionCount() {
		return modIds.length;
	}

}
