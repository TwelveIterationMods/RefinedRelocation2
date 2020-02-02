package net.blay09.mods.refinedrelocation;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class RefinedRelocationConfig {

	public static class Common {
		public final ForgeConfigSpec.ConfigValue<Integer> sortingInterfaceSlotsPerTick;

		Common(ForgeConfigSpec.Builder builder) {
			sortingInterfaceSlotsPerTick = builder
					.comment("The amount of slots each sorting interface should check for changes per tick. Setting this too high while having many interfaces on large inventories can cause performance issues.")
					.translation("refinedrelocation.config.sortingInterfaceSlotsPerTick")
					.define("sortingInterfaceSlotsPerTick", 9);
		}
	}

	public static class Client {
		Client(ForgeConfigSpec.Builder builder) {
		}
	}

	static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;
	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	static final ForgeConfigSpec clientSpec;
	public static final Client CLIENT;
	static {
		final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		clientSpec = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

}
