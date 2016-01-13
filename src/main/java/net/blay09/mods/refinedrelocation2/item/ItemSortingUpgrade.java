package net.blay09.mods.refinedrelocation2.item;

import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSortingUpgrade extends Item {

    public ItemSortingUpgrade() {
        setRegistryName("sorting_upgrade");
        setUnlocalizedName(getRegistryName());
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(ItemModelMesher mesher) {
        mesher.register(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
