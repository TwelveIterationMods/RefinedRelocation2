package net.blay09.mods.refinedrelocation.compat;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public interface RefinedAddon {
	default void preInit() {
	}

	default void init() {
	}

	default void registerBlocks(IForgeRegistry<Block> registry) {
	}

	default void registerItems(IForgeRegistry<Item> registry) {
	}

	default void registerModels() {
	}
}
