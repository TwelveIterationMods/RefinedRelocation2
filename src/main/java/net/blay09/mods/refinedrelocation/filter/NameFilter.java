package net.blay09.mods.refinedrelocation.filter;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.client.IFilterIcon;
import net.blay09.mods.refinedrelocation.api.filter.IConfigurableFilter;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.client.ClientProxy;
import net.blay09.mods.refinedrelocation.client.gui.GuiNameFilter;
import net.blay09.mods.refinedrelocation.container.ContainerNameFilter;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class NameFilter implements IFilter, IConfigurableFilter {

	public static final String ID = RefinedRelocation.MOD_ID + ":name_filter";

	private static final Pattern WILDCARD_PATTERN = Pattern.compile("[^*]+|(\\*)");
	private static final Matcher WILDCARD_MATCHER = WILDCARD_PATTERN.matcher("");

	private String value = "";
	private Pattern[] cachedPatterns;

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
		String itemName = itemStack.getDisplayName();
		Pattern[] patterns = getPatterns();
		for(Pattern pattern : patterns) {
			if(pattern != null && pattern.matcher(itemName).matches()) {
				return true;
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
		if(cachedPatterns == null) {
			String[] patternsSplit = value.split("[\n,]");
			cachedPatterns = new Pattern[patternsSplit.length];
			for(int i = 0; i < patternsSplit.length; i++) {
				WILDCARD_MATCHER.reset(patternsSplit[i]);
				StringBuffer sb = new StringBuffer();
				while(WILDCARD_MATCHER.find()) {
					if(WILDCARD_MATCHER.group(1) != null) {
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
	public NBTBase serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("Patterns", value);
		return compound;
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		NBTTagCompound compound = (NBTTagCompound) nbt;
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
	@SideOnly(Side.CLIENT)
	public IFilterIcon getFilterIcon() {
		return ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:icon_name_filter");
	}

	@Override
	public Container createContainer(EntityPlayer player, TileEntity tileEntity) {
		return new ContainerNameFilter(player, tileEntity, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiScreen createGuiScreen(EntityPlayer player, TileEntity tileEntity) {
		return new GuiNameFilter(player, tileEntity, this);
	}

	@Override
	public int getVisualOrder() {
		return 900;
	}
}
