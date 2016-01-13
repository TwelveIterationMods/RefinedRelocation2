package net.blay09.mods.refinedrelocation2.item;

import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.MultipartRegistry;
import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.part.PartSortingInterface;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSortingInterface extends ItemMultiPart {

    public ItemSortingInterface() {
        setRegistryName("sorting_interface");
        setUnlocalizedName(getRegistryName());
        setCreativeTab(RefinedRelocation2.creativeTab);

        MultipartRegistry.registerPartFactory((s, b) -> {
            return new PartSortingInterface();
        }, getRegistryName());
        GameRegistry.registerItem(this);
    }

    @Override
    public IMultipart createPart(World world, BlockPos blockPos, EnumFacing facing, Vec3 vec3, ItemStack itemStack, EntityPlayer entityPlayer) {
        return new PartSortingInterface(facing);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(ItemModelMesher mesher) {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
        mesher.register(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
