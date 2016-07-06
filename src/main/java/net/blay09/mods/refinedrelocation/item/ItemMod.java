package net.blay09.mods.refinedrelocation.item;

import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMod extends Item {

	@SideOnly(Side.CLIENT)
	public void registerModels(ItemModelMesher mesher) {
		mesher.register(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
