package net.blay09.mods.refinedrelocation.filter;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.TileOrMultipart;
import net.blay09.mods.refinedrelocation.api.client.IFilterIcon;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameFilter implements IFilter {

	public static final String ID = RefinedRelocation.MOD_ID + ":NameFilter";

	private static final Pattern WILDCARD_PATTERN = Pattern.compile("[^*]+|(\\*)");
	private static final Matcher WILDCARD_MATCHER = WILDCARD_PATTERN.matcher("");

	private String value = "";
	private Pattern[] cachedPatterns;

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
		String itemName = itemStack.getDisplayName();
		Pattern[] patterns = getPatterns();
		for(Pattern pattern : patterns) {
			if(pattern.matcher(itemName).matches()) {
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
						WILDCARD_MATCHER.appendReplacement(sb, Matcher.quoteReplacement(WILDCARD_MATCHER.group(0)));
					}
				}
				WILDCARD_MATCHER.appendTail(sb);
				cachedPatterns[i] = Pattern.compile(sb.toString());
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
		return "filter.refinedrelocation:NameFilter";
	}

	@Override
	public String getDescriptionLangKey() {
		return "filter.refinedrelocation:NameFilter.description";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IFilterIcon getFilterIcon() {
		return ClientProxy.TEXTURE_ATLAS.getSprite("refinedrelocation:icon_NameFilter");
	}

	@Override
	public boolean isConfigurable() {
		return false;
	}

	@Override
	public Container createContainer(EntityPlayer player, TileOrMultipart tileEntity) {
		return new ContainerNameFilter(player, tileEntity, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiScreen createGuiScreen(EntityPlayer player, TileOrMultipart tileEntity) {
		return new GuiNameFilter(player, tileEntity, this);
	}
}
