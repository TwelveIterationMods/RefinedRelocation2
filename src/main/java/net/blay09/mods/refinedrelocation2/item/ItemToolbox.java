package net.blay09.mods.refinedrelocation2.item;

import com.google.common.collect.Lists;
import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemToolbox extends Item {

    public static final List<Item> toolboxRegistry = Lists.newArrayList();

    public static ItemStack cachedHeldItem;
    public static ItemStack cachedSelectedStack;

    public ItemToolbox() {
        setRegistryName("toolbox");
        setUnlocalizedName(getRegistryName());
        setMaxStackSize(1);
        setCreativeTab(RefinedRelocation2.creativeTab);
        GameRegistry.registerItem(this);
    }

    @Override
    public boolean doesSneakBypassUse(World world, BlockPos pos, EntityPlayer entityPlayer) {
        ItemStack itemStack = entityPlayer.getHeldItem();
        if(itemStack != null && itemStack.getItem() == this) {
            ItemStack selectedWrench = getSelectedWrench(itemStack);
            if(selectedWrench != null) {
                return selectedWrench.getItem().doesSneakBypassUse(world, pos, entityPlayer);
            }
        }
        return false;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer entityPlayer, EntityLivingBase target) {
        ItemStack selectedWrench = getSelectedWrench(itemStack);
        if(selectedWrench != null) {
            boolean result = selectedWrench.interactWithEntity(entityPlayer, target);
            updateWrench(itemStack, selectedWrench);
            return result;
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        ItemStack selectedWrench = getSelectedWrench(itemStack);
        if(selectedWrench != null) {
            selectedWrench.useItemRightClick(world, entityPlayer);
            updateWrench(itemStack, selectedWrench);
        }
        return itemStack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack selectedWrench = getSelectedWrench(itemStack);
        if(selectedWrench != null) {
            if(!selectedWrench.getItem().onItemUseFirst(itemStack, entityPlayer, world, pos, side, hitX, hitY, hitZ)) {
                IBlockState blockState = world.getBlockState(pos);
                entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem] = selectedWrench;
                blockState.getBlock().onBlockActivated(world, pos, blockState, entityPlayer, side, hitX, hitY, hitZ);
                entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem] = itemStack;
                updateWrench(itemStack, selectedWrench);
            }
        }
        return false;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack selectedWrench = getSelectedWrench(itemStack);
        if(selectedWrench != null) {
            boolean result = selectedWrench.onItemUse(entityPlayer, world, pos, side, hitX, hitY, hitZ);
            updateWrench(itemStack, selectedWrench);
            return result;
        }
        return false;
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.onUpdate(itemStack, world, entity, itemSlot, isSelected);
        if(world.isRemote) {
            if (isSelected) {
                if (cachedHeldItem != itemStack) {
                    cachedHeldItem = itemStack;
                    cachedSelectedStack = getSelectedWrench(cachedHeldItem);
                }
            } else {
                cachedHeldItem = null;
                cachedSelectedStack = null;
            }
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack itemStack) {
        return cachedHeldItem == itemStack && cachedSelectedStack != null && cachedSelectedStack.getItem().showDurabilityBar(cachedSelectedStack);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return cachedSelectedStack != null ? cachedSelectedStack.getItem().getDurabilityForDisplay(cachedSelectedStack) : super.getDurabilityForDisplay(itemStack);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(ItemModelMesher mesher) {
        mesher.register(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    public ItemStack getSelectedWrench(ItemStack itemStack) {
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if(tagCompound == null) {
            return null;
        }
        int selectedIndex = tagCompound.getByte("SelectedIndex");
        if(selectedIndex <= 0) {
            return null;
        }
        selectedIndex--;
        NBTTagList tagList = tagCompound.getTagList("WrenchList", Constants.NBT.TAG_COMPOUND);
        if(selectedIndex < tagList.tagCount()) {
            return ItemStack.loadItemStackFromNBT(tagList.getCompoundTagAt(selectedIndex));
        }
        return null;
    }

    public void updateWrench(ItemStack itemStack, ItemStack wrenchStack) {
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if(tagCompound == null) {
            return;
        }
        int selectedIndex = tagCompound.getByte("SelectedIndex");
        if(selectedIndex <= 0) {
            return;
        }
        selectedIndex--;
        NBTTagList tagList = tagCompound.getTagList("WrenchList", Constants.NBT.TAG_COMPOUND);
        if(wrenchStack.stackSize <= 0) {
            tagList.removeTag(selectedIndex);
        } else {
            if (selectedIndex < tagList.tagCount()) {
                NBTTagCompound itemCompound = new NBTTagCompound();
                wrenchStack.writeToNBT(itemCompound);
                tagList.set(selectedIndex, itemCompound);
            }
        }
        tagCompound.setTag("WrenchList", tagList);
        cachedSelectedStack = wrenchStack;
    }

}
