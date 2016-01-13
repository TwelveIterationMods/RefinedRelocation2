package net.blay09.mods.refinedrelocation2.item.toolbox;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.item.IScrollableItem;
import net.blay09.mods.refinedrelocation2.network.GuiHandler;
import net.blay09.mods.refinedrelocation2.network.MessageScrollIndex;
import net.blay09.mods.refinedrelocation2.network.NetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Set;

public class ItemToolbox extends Item implements IScrollableItem {

    public static final List<Item> toolboxRegistry = Lists.newArrayList();

    private final ToolboxCache toolboxCache = new ToolboxCache();

    public ItemToolbox() {
        setRegistryName("toolbox");
        setUnlocalizedName(getRegistryName());
        setMaxStackSize(1);
        setCreativeTab(RefinedRelocation2.creativeTab);
        GameRegistry.registerItem(this);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(ItemStack itemStack) {
        ItemStack activeStack = toolboxCache.getActiveStack(itemStack);
        if(activeStack != null) {
            return activeStack.getAttributeModifiers();
        }
        return super.getAttributeModifiers(itemStack);
    }

    @Override
    public float getDigSpeed(ItemStack itemStack, IBlockState state) {
        ItemStack activeStack = toolboxCache.getActiveStack(itemStack);
        if(activeStack != null) {
            return activeStack.getItem().getDigSpeed(activeStack, state);
        }
        return super.getDigSpeed(itemStack, state);
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker) {
        ItemStack activeStack = toolboxCache.getActiveStack(attacker, itemStack);
        if(activeStack != null) {
            activeStack.hitEntity(target, (EntityPlayer) attacker);
            updateActiveStack(itemStack, activeStack);
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, BlockPos pos, EntityLivingBase entityPlayer) {
        ItemStack activeStack = toolboxCache.getActiveStack(entityPlayer, itemStack);
        if(activeStack != null) {
            activeStack.onBlockDestroyed(world, block, pos, (EntityPlayer) entityPlayer);
            updateActiveStack(itemStack, activeStack);
        }
        return false;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        ItemStack activeStack = toolboxCache.getActiveStack(itemStack);
        if(activeStack != null) {
            return activeStack.getItemUseAction();
        }
        return EnumAction.NONE;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        ItemStack activeStack = toolboxCache.getActiveStack(itemStack);
        if(activeStack != null) {
            return activeStack.getMaxItemUseDuration();
        }
        return 0;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer entityPlayer, int timeLeft) {
        ItemStack activeStack = toolboxCache.getActiveStack(entityPlayer, itemStack);
        if(activeStack != null) {
            activeStack.onPlayerStoppedUsing(world, entityPlayer, timeLeft);
            updateActiveStack(itemStack, activeStack);
        }
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemStack, BlockPos pos, EntityPlayer entityPlayer) {
        ItemStack activeStack = toolboxCache.getActiveStack(entityPlayer, itemStack);
        if(activeStack != null) {
            boolean result = activeStack.getItem().onBlockStartBreak(itemStack, pos, entityPlayer);
            updateActiveStack(itemStack, activeStack);
            return result;
        }
        return false;
    }

    @Override
    public int getHarvestLevel(ItemStack itemStack, String toolClass) {
        ItemStack activeStack = toolboxCache.getActiveStack(itemStack);
        if(activeStack != null) {
            return activeStack.getItem().getHarvestLevel(itemStack, toolClass);
        }
        return super.getHarvestLevel(itemStack, toolClass);
    }

    @Override
    public Set<String> getToolClasses(ItemStack itemStack) {
        ItemStack activeStack = toolboxCache.getActiveStack(itemStack);
        if(activeStack != null) {
            return activeStack.getItem().getToolClasses(itemStack);
        }
        return super.getToolClasses(itemStack);
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack itemStack) {
        ItemStack activeStack = toolboxCache.getActiveStack(itemStack);
        if(activeStack != null) {
            return activeStack.getItem().canHarvestBlock(block, itemStack);
        }
        return super.canHarvestBlock(block, itemStack);
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack itemStack) {
        if(entityLiving instanceof EntityPlayer) {
            ItemStack activeStack = toolboxCache.getActiveStack(entityLiving, itemStack);
            if(activeStack != null) {
                boolean result = activeStack.getItem().onEntitySwing(entityLiving, itemStack);
                updateActiveStack(itemStack, activeStack);
                return result;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack itemStack, EntityPlayer entityPlayer, Entity entity) {
        ItemStack activeStack = toolboxCache.getActiveStack(entityPlayer, itemStack);
        if(activeStack != null) {
            boolean result = activeStack.getItem().onLeftClickEntity(itemStack, entityPlayer, entity);
            updateActiveStack(itemStack, activeStack);
            return result;
        }
        return false;
    }

    @Override
    public void onUsingTick(ItemStack itemStack, EntityPlayer entityPlayer, int count) {
        ItemStack activeStack = toolboxCache.getActiveStack(entityPlayer, itemStack);
        if(activeStack != null) {
            activeStack.getItem().onUsingTick(itemStack, entityPlayer, count);
        }
    }

    @Override
    public boolean doesSneakBypassUse(World world, BlockPos pos, EntityPlayer entityPlayer) {
        ItemStack itemStack = entityPlayer.getHeldItem();
        if(itemStack != null && itemStack.getItem() == this) {
            ItemStack activeStack = toolboxCache.getActiveStack(entityPlayer, itemStack);
            if(activeStack != null) {
                return activeStack.getItem().doesSneakBypassUse(world, pos, entityPlayer);
            }
        }
        return false;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer entityPlayer, EntityLivingBase target) {
        ItemStack activeStack = toolboxCache.getActiveStack(entityPlayer, itemStack);
        if(activeStack != null) {
            boolean result = activeStack.interactWithEntity(entityPlayer, target);
            updateActiveStack(itemStack, activeStack);
            return result;
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        ItemStack activeStack = toolboxCache.getActiveStack(entityPlayer, itemStack);
        if(activeStack != null) {
            activeStack.useItemRightClick(world, entityPlayer);
            updateActiveStack(itemStack, activeStack);
        } else {
            entityPlayer.openGui(RefinedRelocation2.instance, GuiHandler.GUI_TOOLBOX, world, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
        }
        return itemStack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack activeStack = toolboxCache.getActiveStack(entityPlayer, itemStack);
        if(activeStack != null) {
            int oldStackSize = activeStack.stackSize;
            if(!activeStack.getItem().onItemUseFirst(itemStack, entityPlayer, world, pos, side, hitX, hitY, hitZ)) {
                IBlockState blockState = world.getBlockState(pos);
                entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem] = activeStack;
                blockState.getBlock().onBlockActivated(world, pos, blockState, entityPlayer, side, hitX, hitY, hitZ);
                entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem] = itemStack;
                updateActiveStack(itemStack, activeStack);
            }
            if(oldStackSize != activeStack.stackSize) {
                RefinedRelocation2.proxy.showItemHighlight();
            }
        } else {
            entityPlayer.openGui(RefinedRelocation2.instance, GuiHandler.GUI_TOOLBOX, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return false;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack activeStack = toolboxCache.getActiveStack(entityPlayer, itemStack);
        if(activeStack != null) {
            int oldStackSize = activeStack.stackSize;
            boolean result = activeStack.onItemUse(entityPlayer, world, pos, side, hitX, hitY, hitZ);
            updateActiveStack(itemStack, activeStack);
            if(oldStackSize != activeStack.stackSize) {
                RefinedRelocation2.proxy.showItemHighlight();
            }
            return result;
        }
        return false;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        ItemStack activeStack = toolboxCache.getActiveStack(entityPlayer, itemStack);
        if(activeStack != null) {
            activeStack.onItemUseFinish(world, entityPlayer);
            updateActiveStack(itemStack, activeStack);
        }
        return itemStack;
    }

    @Override
    public void onScrolled(EntityPlayer entityPlayer, ItemStack itemStack, int delta) {
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if(tagCompound == null) {
            return;
        }
        int selectedIndex = tagCompound.getByte("SelectedIndex");
        int oldIndex = selectedIndex;
        selectedIndex += (delta > 0 ? 1 : -1);
        int maxIndex = tagCompound.getTagList("ItemList", Constants.NBT.TAG_COMPOUND).tagCount();
        if(selectedIndex > maxIndex) {
            selectedIndex = 0;
        } else if(selectedIndex < 0) {
            selectedIndex = maxIndex;
        }
        if(selectedIndex != oldIndex) {
            tagCompound.setByte("SelectedIndex", (byte) selectedIndex);
            toolboxCache.refreshActiveStack(entityPlayer, itemStack);
            if(entityPlayer.getEntityWorld().isRemote) {
                NetworkHandler.instance.sendToServer(new MessageScrollIndex(selectedIndex));
            }
        }
    }

    @Override
    public void setScrollIndex(EntityPlayer entityPlayer, ItemStack itemStack, int index) {
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if(tagCompound == null) {
            return;
        }
        if(index >= 0 && index <= tagCompound.getTagList("ItemList", Constants.NBT.TAG_COMPOUND).tagCount()) {
            tagCompound.setByte("SelectedIndex", (byte) index);
            toolboxCache.refreshActiveStack(entityPlayer, itemStack);
            if(entityPlayer.getEntityWorld().isRemote) {
                NetworkHandler.instance.sendToServer(new MessageScrollIndex(index));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean showDurabilityBar(ItemStack itemStack) {
        EntityPlayer entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
        ItemStack activeStack = toolboxCache.getActiveStack(entityPlayer, itemStack);
        return entityPlayer.getHeldItem() == itemStack && activeStack != null && activeStack.getItem().showDurabilityBar(activeStack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getDurabilityForDisplay(ItemStack itemStack) {
        EntityPlayer entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
        ItemStack activeStack = toolboxCache.getActiveStack(entityPlayer, itemStack);
        return activeStack != null ? activeStack.getItem().getDurabilityForDisplay(activeStack) : super.getDurabilityForDisplay(itemStack);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> tooltip, boolean advanced) {
        ItemStack activeStack = toolboxCache.getActiveStack(entityPlayer, itemStack);
        if (activeStack != null) {
            tooltip.add("\u00a7eActive Tool: \u00a7r" + activeStack.getDisplayName());
        } else {
            tooltip.add("\u00a7eActive Tool: \u00a7rNone");
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getHighlightTip(ItemStack itemStack, String displayName) {
        ItemStack activeStack = toolboxCache.getActiveStack(FMLClientHandler.instance().getClientPlayerEntity(), itemStack);
        return activeStack != null ? (displayName + " (\u00a7e" + (activeStack.isStackable() ? (activeStack.stackSize + "x ") : "") + activeStack.getDisplayName() + "\u00a7r)") : displayName + " (\u00a7eNone\u00a7r)";
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(ItemModelMesher mesher) {
        mesher.register(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    public void updateActiveStack(ItemStack itemStack, ItemStack activeStack) {
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if(tagCompound == null) {
            return;
        }
        int selectedIndex = tagCompound.getByte("SelectedIndex");
        if(selectedIndex <= 0) {
            return;
        }
        selectedIndex--;
        NBTTagList tagList = tagCompound.getTagList("ItemList", Constants.NBT.TAG_COMPOUND);
        if (selectedIndex < tagList.tagCount()) {
            if(activeStack.stackSize <= 0) {
                tagList.removeTag(selectedIndex);
                tagCompound.setByte("SelectedIndex", (byte) 0);
            } else {
                NBTTagCompound itemCompound = new NBTTagCompound();
                activeStack.writeToNBT(itemCompound);
                tagList.set(selectedIndex, itemCompound);
            }
        }
        tagCompound.setTag("ItemList", tagList);
    }

    public IInventory getInventory(EntityPlayer entityPlayer) {
        return new ToolboxInventory(entityPlayer);
    }

}
