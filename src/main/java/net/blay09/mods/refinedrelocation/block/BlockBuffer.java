package net.blay09.mods.refinedrelocation.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockBuffer extends BlockModTile {

	public BlockBuffer() {
		super(Material.IRON, "buffer");
		setSoundType(SoundType.METAL);
		setHardness(3f);
	}


}
