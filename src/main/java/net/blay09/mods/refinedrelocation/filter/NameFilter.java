package net.blay09.mods.refinedrelocation.filter;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;
import net.blay09.mods.refinedrelocation.util.TileWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameFilter implements IFilter {

	public static final String ID = RefinedRelocation.MOD_ID + ":NameFilter";

	private static final Pattern WILDCARD_PATTERN = Pattern.compile("[^*]+|(\\*)");
	private static final Matcher WILDCARD_MATCHER = WILDCARD_PATTERN.matcher("");

	private String patterns = "";
	private Pattern[] cachedPatterns;

	@Override
	public String getIdentifier() {
		return ID;
	}

	@Override
	public boolean isFilterUsable(TileWrapper pos) {
		return true;
	}

	@Override
	public boolean passes(TileWrapper tilePos, ItemStack itemStack) {
		String itemName = itemStack.getDisplayName();
		Pattern[] patterns = getPatterns();
		for(Pattern pattern : patterns) {
			if(pattern.matcher(itemName).matches()) {
				return true;
			}
		}
		return false;
	}

	public Pattern[] getPatterns() {
		if(cachedPatterns == null) {
			String[] patternsSplit = patterns.split("[\n,]");
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
		compound.setString("Patterns", patterns);
		return compound;
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		NBTTagCompound compound = (NBTTagCompound) nbt;
		patterns = compound.getString("Patterns");
	}
}
