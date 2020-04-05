package net.blay09.mods.refinedrelocation.filter;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.client.IDrawable;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.client.gui.GuiTextures;
import net.blay09.mods.refinedrelocation.container.NameFilterContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class NameFilter implements IFilter {

    public static final String ID = RefinedRelocation.MOD_ID + ":name_filter";

    private static final Pattern WILDCARD_PATTERN = Pattern.compile("[^*]+|(\\*)");
    private static final Matcher WILDCARD_MATCHER = WILDCARD_PATTERN.matcher("");

    public enum NameFilterType {
        NAME,
        TAG,
        MOD
    }

    private String value = "";
    private Pattern[] cachedPatterns;
    private NameFilterType[] cachedNameFilterType;

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
        String itemName = null;
        Collection<ResourceLocation> tags = null;
        Pattern[] patterns = getPatterns();
        for (int i = 0; i < patterns.length; i++) {
            Pattern pattern = patterns[i];
            if (pattern != null) {
                if (cachedNameFilterType[i] == NameFilterType.TAG) {
                    if (tags == null) {
                        tags = ItemTags.getCollection().getOwningTags(itemStack.getItem());
                    }
                    Matcher matcher = pattern.matcher("");
                    for (ResourceLocation tag : tags) {
                        matcher.reset(tag.toString());
                        if (matcher.matches()) {
                            return true;
                        }

                        matcher.reset(tag.getPath());
                        if (matcher.matches()) {
                            return true;
                        }
                    }
                } else if (cachedNameFilterType[i] == NameFilterType.MOD) {
                    ResourceLocation registryName = itemStack.getItem().getRegistryName();
                    if (registryName != null) {
                        Matcher matcher = pattern.matcher(registryName.getNamespace());
                        if (matcher.matches()) {
                            return true;
                        }
                    }
                } else {
                    if (itemName == null) {
                        itemName = itemStack.getDisplayName().getUnformattedComponentText();
                    }
                    if (pattern.matcher(itemName).matches()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setValue(String value) {
        this.value = value;
        this.cachedPatterns = null;
    }

    public String getValue() {
        return value;
    }

    public Pattern[] getPatterns() {
        if (cachedPatterns == null) {
            String[] patternsSplit = value.split("[\n,]");
            cachedPatterns = new Pattern[patternsSplit.length];
            cachedNameFilterType = new NameFilterType[patternsSplit.length];
            for (int i = 0; i < patternsSplit.length; i++) {
                if ((patternsSplit[i].startsWith("tag:") || patternsSplit[i].startsWith("ore:")) && patternsSplit[i].length() > 4) {
                    patternsSplit[i] = patternsSplit[i].substring(4);
                    cachedNameFilterType[i] = NameFilterType.TAG;
                } else if (patternsSplit[i].startsWith("mod:") && patternsSplit[i].length() > 4) {
                    patternsSplit[i] = patternsSplit[i].substring(4).toLowerCase(Locale.ENGLISH).replace(' ', '*');
                    cachedNameFilterType[i] = NameFilterType.MOD;
                }
                WILDCARD_MATCHER.reset(patternsSplit[i]);
                StringBuffer sb = new StringBuffer();
                while (WILDCARD_MATCHER.find()) {
                    if (WILDCARD_MATCHER.group(1) != null) {
                        WILDCARD_MATCHER.appendReplacement(sb, ".*");
                    } else {
                        WILDCARD_MATCHER.appendReplacement(sb, "\\\\Q" + WILDCARD_MATCHER.group(0) + "\\\\E");
                    }
                }
                WILDCARD_MATCHER.appendTail(sb);
                try {
                    cachedPatterns[i] = Pattern.compile(sb.toString());
                } catch (PatternSyntaxException e) {
                    RefinedRelocation.logger.error("Caught an exception in the pattern compilation for the Name Filter. This should never happen, please report: {} => {}", patternsSplit[i], sb.toString());
                }
            }
        }
        return cachedPatterns;
    }

    @Override
    public INBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.putString("Patterns", value);
        return compound;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CompoundNBT compound = (CompoundNBT) nbt;
        value = compound.getString("Patterns");
    }

    @Override
    public String getLangKey() {
        return "filter.refinedrelocation:name_filter";
    }

    @Override
    public String getDescriptionLangKey() {
        return "filter.refinedrelocation:name_filter.description";
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public IDrawable getFilterIcon() {
        return GuiTextures.NAME_FILTER_ICON;
    }

    @Override
    public int getVisualOrder() {
        return 900;
    }

    @Nullable
    @Override
    public INamedContainerProvider getConfiguration(PlayerEntity player, TileEntity tileEntity, int rootFilterIndex) {
        return new INamedContainerProvider() {
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new NameFilterContainer(i, playerInventory, tileEntity, NameFilter.this);
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
