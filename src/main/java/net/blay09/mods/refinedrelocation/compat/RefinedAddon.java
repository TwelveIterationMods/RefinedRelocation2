package net.blay09.mods.refinedrelocation.compat;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class RefinedAddon {
	public void preInit() {

	}
	@SideOnly(Side.CLIENT)
	public void preInitClient() {

	}

	public void init() {

	}
}
