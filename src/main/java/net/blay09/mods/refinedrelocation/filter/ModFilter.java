package net.blay09.mods.refinedrelocation.filter;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.client.IDrawable;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class ModFilter implements IChecklistFilter {

    public static final String ID = RefinedRelocation.MOD_ID + ":mod_filter";

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
        for (ResourceLocation registryName : ForgeRegistries.ITEMS.getKeys()) {
            modSet.add(registryName.getPath());
        }
        String[] unsorted = modSet.toArray(new String[0]);
        Arrays.sort(unsorted, (o1, o2) -> {
            if (o1.equals("minecraft")) {
                return -1;
            } else if (o2.equals("minecraft")) {
                return 1;
            }
            return o2.compareTo(o1);
        });
        setModList(unsorted);
    }

    public static void setModList(String[] modIDs) {
        ModFilter.modIds = modIDs;
        modList.clear();
        for (int i = 0; i < modIDs.length; i++) {
            if (modIDs[i].equals("minecraft")) {
                modList.put(modIDs[i], new ModWithName(i, modIDs[i], "Minecraft"));
            } else {
                ModContainer modContainer = ModList.get().getModContainerById(modIDs[i]).orElse(null);
                modList.put(modIDs[i], new ModWithName(i, modIDs[i], modContainer != null ? modContainer.getModInfo().getDisplayName() : modIDs[i]));
            }
        }
    }

    private final boolean[] modStates = new boolean[modIds.length];

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
        ResourceLocation resourceLocation = itemStack.getItem().getRegistryName();
        if (resourceLocation != null) {
            ModWithName modWithName = modList.get(resourceLocation.getPath());
            return modWithName != null && modStates[modWithName.getIndex()];
        }
        return false;
    }

    @Override
    public INBTBase serializeNBT() {
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < modStates.length; i++) {
            if (modStates[i]) {
                list.add(new NBTTagString(modIds[i]));
            }
        }
        return list;
    }

    @Override
    public void deserializeNBT(INBTBase nbt) {
        NBTTagList list = (NBTTagList) nbt;
        for (int i = 0; i < list.size(); i++) {
            String modId = list.getString(i);
            for (int j = 0; j < modIds.length; j++) {
                if (modIds[j].equals(modId)) {
                    modStates[j] = true;
                }
            }
        }
    }

    @Override
    public String getLangKey() {
        return "filter.refinedrelocation:mod_filter";
    }

    @Override
    public String getDescriptionLangKey() {
        return "filter.refinedrelocation:mod_filter.description";
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public IDrawable getFilterIcon() {
        return GuiTextures.MOD_FILTER_ICON;
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

    @Override
    public int getVisualOrder() {
        return 800;
    }
}
