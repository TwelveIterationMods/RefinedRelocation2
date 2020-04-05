package net.blay09.mods.refinedrelocation.compat;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.IForgeRegistry;

public interface RefinedRelocationAddon {
	default void preInit() {
	}

	default void init() {
	}

	default void registerBlocks(IForgeRegistry<Block> registry) {
	}

	default void registerItems(IForgeRegistry<Item> registry) {
	}

	default void registerTileEntities(IForgeRegistry<TileEntityType<?>> registry) {
	}

	default void setupClient() {
	}
}
