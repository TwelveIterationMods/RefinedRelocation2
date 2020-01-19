package net.blay09.mods.refinedrelocation.filter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.client.IDrawable;
import net.blay09.mods.refinedrelocation.api.filter.IChecklistFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.blay09.mods.refinedrelocation.container.ChecklistFilterContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.Collection;
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

        public abstract boolean passes(ItemStack itemStack, Collection<ResourceLocation> tags);
    }

    private static final Map<String, Preset> presetMap = Maps.newHashMap();
    private static final List<Preset> presetList = Lists.newArrayList();

    public static final Preset ORES = new Preset("ores") {
        @Override
        public boolean passes(ItemStack itemStack, Collection<ResourceLocation> tags) {
            for (ResourceLocation tag : tags) {
                if (tag.getPath().equals("ores")) {
                    return true;
                }
            }
            return false;
        }
    };

    public static final Preset INGOTS = new Preset("ingots") {
        @Override
        public boolean passes(ItemStack itemStack, Collection<ResourceLocation> tags) {
            for (ResourceLocation tag : tags) {
                if (tag.getPath().equals("ingots")) {
                    return true;
                }
            }
            return false;
        }
    };

    public static final Preset NUGGETS = new Preset("nuggets") {
        @Override
        public boolean passes(ItemStack itemStack, Collection<ResourceLocation> tags) {
            for (ResourceLocation tag : tags) {
                if (tag.getPath().equals("nuggets")) {
                    return true;
                }
            }
            return false;
        }
    };

    public static final Preset GEMS = new Preset("gems") {
        @Override
        public boolean passes(ItemStack itemStack, Collection<ResourceLocation> tags) {
            for (ResourceLocation tag : tags) {
                if (tag.getPath().equals("gems")) {
                    return true;
                }
            }
            return false;
        }
    };

    public static final Preset DYES = new Preset("dyes") {
        @Override
        public boolean passes(ItemStack itemStack, Collection<ResourceLocation> tags) {
            for (ResourceLocation tag : tags) {
                if (tag.getPath().equals("dyes")) {
                    return true;
                }
            }
            return false;
        }
    };

    public static final Preset FOOD = new Preset("food") {
        @Override
        public boolean passes(ItemStack itemStack, Collection<ResourceLocation> tags) {
            return itemStack.getItem().isFood();
        }
    };

    public static final Preset FUEL_ITEMS = new Preset("fuel") {
        @Override
        public boolean passes(ItemStack itemStack, Collection<ResourceLocation> tags) {
            int burnTime = itemStack.getBurnTime();
            burnTime = burnTime == -1 ? ForgeHooks.getBurnTime(itemStack) : burnTime;
            return ForgeEventFactory.getItemBurnTime(itemStack, burnTime) > 0;
        }
    };

    public static final Preset BLOCKS = new Preset("blocks") {
        @Override
        public boolean passes(ItemStack itemStack, Collection<ResourceLocation> tags) {
            return itemStack.getItem() instanceof BlockItem;
        }
    };

    public static final Preset UNSTACKABLE = new Preset("unstackable") {
        @Override
        public boolean passes(ItemStack itemStack, Collection<ResourceLocation> tags) {
            return itemStack.getMaxStackSize() <= 1;
        }
    };

    public static final Preset REPAIRABLE = new Preset("repairable") {
        @Override
        public boolean passes(ItemStack itemStack, Collection<ResourceLocation> tags) {
            return itemStack.getItem().isRepairable(itemStack);
        }
    };

    public static final Preset WEAPONS = new Preset("weapons") {
        @Override
        public boolean passes(ItemStack itemStack, Collection<ResourceLocation> tags) {
            return itemStack.getItem() instanceof SwordItem || itemStack.getItem() instanceof BowItem;
        }
    };

    public static final Preset TOOLS = new Preset("tools") {
        @Override
        public boolean passes(ItemStack itemStack, Collection<ResourceLocation> tags) {
            return itemStack.getItem() == Items.CARROT_ON_A_STICK || itemStack.getItem() == Items.FLINT_AND_STEEL || itemStack.getItem() instanceof ToolItem || itemStack.getItem() instanceof FishingRodItem || itemStack.getItem() instanceof ShearsItem;
        }
    };

    public static final Preset ENCHANTED = new Preset("enchanted") {
        @Override
        public boolean passes(ItemStack itemStack, Collection<ResourceLocation> tags) {
            return itemStack.isEnchanted();
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
        Collection<ResourceLocation> tags = ItemTags.getCollection().getOwningTags(itemStack.getItem());
        for (int i = 0; i < presetList.size(); i++) {
            if (presetStates[i] && presetList.get(i).passes(itemStack, tags)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public INBT serializeNBT() {
        ListNBT list = new ListNBT();
        for (int i = 0; i < presetStates.length; i++) {
            if (presetStates[i]) {
                list.add(StringNBT.func_229705_a_(presetList.get(i).getId()));
            }
        }
        return list;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        ListNBT list = (ListNBT) nbt;
        for (int i = 0; i < list.size(); i++) {
            Preset preset = presetMap.get(list.getString(i));
            if (preset != null) {
                int index = presetList.indexOf(preset);
                if (index != -1) {
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
    @OnlyIn(Dist.CLIENT)
    public IDrawable getFilterIcon() {
        return GuiTextures.PRESET_FILTER_ICON;
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

    @Nullable
    @Override
    public INamedContainerProvider getConfiguration(PlayerEntity player, TileEntity tileEntity) {
        return new INamedContainerProvider() {
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new ChecklistFilterContainer(i, playerInventory, tileEntity, PresetFilter.this);
            }

            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("refinedrelocation:any_filter");
            }
        };
    }

    @Override
    public boolean hasConfiguration() {
        return true;
    }
}
