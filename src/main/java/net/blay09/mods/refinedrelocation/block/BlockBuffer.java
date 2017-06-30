package net.blay09.mods.refinedrelocation.block;

import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class BlockBuffer extends BlockModTile {

	public static String name = "buffer";
	public static final ResourceLocation registryName = new ResourceLocation(RefinedRelocation.MOD_ID, name);

	public BlockBuffer() {
		super(Material.IRON);
		setUnlocalizedName(registryName.toString());
		setSoundType(SoundType.METAL);
		setHardness(3f);
	}


}
