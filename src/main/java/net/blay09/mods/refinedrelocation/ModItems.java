package net.blay09.mods.refinedrelocation;

import net.blay09.mods.refinedrelocation.item.ItemSortingUpgrade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

	public static ItemSortingUpgrade sortingUpgrade;

	public static void init() {
		sortingUpgrade = new ItemSortingUpgrade();
		GameRegistry.register(sortingUpgrade);
	}

	@SideOnly(Side.CLIENT)
	public static void registerModels() {
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		sortingUpgrade.registerModels(mesher);
	}

}
