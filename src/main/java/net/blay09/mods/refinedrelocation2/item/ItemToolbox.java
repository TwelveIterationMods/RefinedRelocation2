package net.blay09.mods.refinedrelocation2.item;

import com.google.common.collect.Lists;
import net.blay09.mods.refinedrelocation2.ModItems;
import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.network.GuiHandler;
import net.blay09.mods.refinedrelocation2.network.MessageScrollIndex;
import net.blay09.mods.refinedrelocation2.network.NetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Set;

/**
 * TODO Cache selected toolbox items based on some UUID system so we don't have to load a new ItemStack from NBT for every action
  */
public class ItemToolbox extends Item implements IScrollableItem {

    public static final List<Item> toolboxRegistry = Lists.newArrayList();

    public ItemStack clientHeldItem;
    public ItemStack clientHeldSelectedStack;
    public ItemStack clientHoverItem;
    public ItemStack clientHoverSelectedStack;
    public ItemStack clientHighlightItem;
    public ItemStack clientHighlightSelectedStack;

    public ItemToolbox() {
        setRegistryName("toolbox");
        setUnlocalizedName(getRegistryName());
        setMaxStackSize(1);
        setCreativeTab(RefinedRelocation2.creativeTab);
        GameRegistry.registerItem(this);
    }

    @Override
    public float getDigSpeed(ItemStack itemStack, IBlockState state) {
        ItemStack activeStack = getActiveStack(itemStack);
        if(activeStack != null) {
            return activeStack.getItem().getDigSpeed(activeStack, state);
        }
        return super.getDigSpeed(itemStack, state);
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker) {
        ItemStack activeStack = getActiveStack(itemStack);
        if(activeStack != null) {
            activeStack.hitEntity(target, (EntityPlayer) attacker);
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, BlockPos pos, EntityLivingBase entityPlayer) {
        ItemStack activeStack = getActiveStack(itemStack);
        if(activeStack != null) {
            activeStack.onBlockDestroyed(world, block, pos, (EntityPlayer) entityPlayer);
        }
        return false;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        ItemStack activeStack = getActiveStack(itemStack);
        if(activeStack != null) {
            return activeStack.getItemUseAction();
        }
        return EnumAction.NONE;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        ItemStack activeStack = getActiveStack(itemStack);
        if(activeStack != null) {
            return activeStack.getMaxItemUseDuration();
        }
        return 0;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer entityPlayer, int timeLeft) {
        ItemStack activeStack = getActiveStack(itemStack);
        if(activeStack != null) {
            activeStack.onPlayerStoppedUsing(world, entityPlayer, timeLeft);
        }
    }

    @Override
    public boolean hasEffect(ItemStack itemStack) {
        ItemStack activeStack = getActiveStack(itemStack);
        if(activeStack != null) {
            return activeStack.hasEffect();
        }
        return super.hasEffect(itemStack);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemStack, BlockPos pos, EntityPlayer player) {
        ItemStack activeStack = getActiveStack(itemStack);
        return activeStack != null && activeStack.getItem().onBlockStartBreak(itemStack, pos, player);
    }

    @Override
    public int getHarvestLevel(ItemStack itemStack, String toolClass) {
        ItemStack activeStack = getActiveStack(itemStack);
        if(activeStack != null) {
            return activeStack.getItem().getHarvestLevel(itemStack, toolClass);
        }
        return super.getHarvestLevel(itemStack, toolClass);
    }

    @Override
    public Set<String> getToolClasses(ItemStack itemStack) {
        ItemStack activeStack = getActiveStack(itemStack);
        if(activeStack != null) {
            return activeStack.getItem().getToolClasses(itemStack);
        }
        return super.getToolClasses(itemStack);
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack itemStack) {
        ItemStack activeStack = getActiveStack(itemStack);
        if(activeStack != null) {
            return activeStack.getItem().canHarvestBlock(block, itemStack);
        }
        return super.canHarvestBlock(block, itemStack);
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack itemStack) {
        ItemStack activeStack = getActiveStack(itemStack);
        return activeStack != null && activeStack.getItem().onEntitySwing(entityLiving, itemStack);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack itemStack, EntityPlayer player, Entity entity) {
        ItemStack activeStack = getActiveStack(itemStack);
        return activeStack != null && activeStack.getItem().onLeftClickEntity(itemStack, player, entity);
    }

    @Override
    public void onUsingTick(ItemStack itemStack, EntityPlayer entityPlayer, int count) {
        ItemStack activeStack = getActiveStack(itemStack);
        if(activeStack != null) {
            activeStack.getItem().onUsingTick(itemStack, entityPlayer, count);
        }
    }

    @Override
    public boolean doesSneakBypassUse(World world, BlockPos pos, EntityPlayer entityPlayer) {
        ItemStack itemStack = entityPlayer.getHeldItem();
        if(itemStack != null && itemStack.getItem() == this) {
            ItemStack activeStack = getActiveStack(itemStack);
            if(activeStack != null) {
                return activeStack.getItem().doesSneakBypassUse(world, pos, entityPlayer);
            }
        }
        return false;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer entityPlayer, EntityLivingBase target) {
        ItemStack activeStack = getActiveStack(itemStack);
        if(activeStack != null) {
            boolean result = activeStack.interactWithEntity(entityPlayer, target);
            updateWrench(itemStack, activeStack);
            return result;
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        ItemStack activeStack = getActiveStack(itemStack);
        if(activeStack != null) {
            activeStack.useItemRightClick(world, entityPlayer);
            updateWrench(itemStack, activeStack);
        } else {
            entityPlayer.openGui(RefinedRelocation2.instance, GuiHandler.GUI_TOOLBOX, world, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
        }
        return itemStack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack activeStack = getActiveStack(itemStack);
        if(activeStack != null) {
            if(!activeStack.getItem().onItemUseFirst(itemStack, entityPlayer, world, pos, side, hitX, hitY, hitZ)) {
                IBlockState blockState = world.getBlockState(pos);
                entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem] = activeStack;
                blockState.getBlock().onBlockActivated(world, pos, blockState, entityPlayer, side, hitX, hitY, hitZ);
                entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem] = itemStack;
                updateWrench(itemStack, activeStack);
            }
        } else {
            entityPlayer.openGui(RefinedRelocation2.instance, GuiHandler.GUI_TOOLBOX, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return false;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack activeStack = getActiveStack(itemStack);
        if(activeStack != null) {
            boolean result = activeStack.onItemUse(entityPlayer, world, pos, side, hitX, hitY, hitZ);
            updateWrench(itemStack, activeStack);
            return result;
        }
        return false;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        ItemStack activeStack = getActiveStack(itemStack);
        if(activeStack != null) {
            activeStack.onItemUseFinish(world, entityPlayer);
        }
        return itemStack;
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.onUpdate(itemStack, world, entity, itemSlot, isSelected);
        if(world.isRemote) {
            if (isSelected) {
                if (clientHeldItem != itemStack) {
                    clientHeldItem = itemStack;
                    clientHeldSelectedStack = getActiveStack(itemStack);
                }
            } else {
                clientHeldItem = null;
                clientHeldSelectedStack = null;
            }
        }
    }

    @Override
    public void onScrolled(EntityPlayer entityPlayer, ItemStack itemStack, int delta) {
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if(tagCompound == null) {
            return;
        }
        int selectedIndex = tagCompound.getByte("SelectedIndex");
        selectedIndex += (delta > 0 ? 1 : -1);
        int maxIndex = tagCompound.getTagList("ItemList", Constants.NBT.TAG_COMPOUND).tagCount();
        if(selectedIndex > maxIndex) {
            selectedIndex = 0;
        } else if(selectedIndex < 0) {
            selectedIndex = maxIndex;
        }
        tagCompound.setByte("SelectedIndex", (byte) selectedIndex);
        NetworkHandler.instance.sendToServer(new MessageScrollIndex(selectedIndex));
    }

    @Override
    public void receiveScrollIndex(EntityPlayer entityPlayer, ItemStack itemStack, int index) {
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if(tagCompound == null) {
            return;
        }
        if(index >= 0 && index <= tagCompound.getTagList("ItemList", Constants.NBT.TAG_COMPOUND).tagCount()) {
            tagCompound.setByte("SelectedIndex", (byte) index);
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack itemStack) {
        return clientHeldItem == itemStack && clientHeldSelectedStack != null && clientHeldSelectedStack.getItem().showDurabilityBar(clientHeldSelectedStack);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return clientHeldSelectedStack != null ? clientHeldSelectedStack.getItem().getDurabilityForDisplay(clientHeldSelectedStack) : super.getDurabilityForDisplay(itemStack);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> tooltip, boolean advanced) {
        if(clientHoverItem != itemStack) {
            clientHoverItem = itemStack;
            clientHoverSelectedStack = getActiveStack(itemStack);
        }
        if (clientHoverSelectedStack != null) {
            tooltip.add("\u00a7eActive Tool: \u00a7r" + clientHoverSelectedStack.getDisplayName());
        } else {
            tooltip.add("\u00a7eActive Tool: \u00a7rNone");
        }
    }

    @Override
    public String getHighlightTip(ItemStack itemStack, String displayName) {
        if(clientHighlightItem != itemStack) {
            clientHighlightItem = itemStack;
            clientHighlightSelectedStack = getActiveStack(itemStack);
        }
        return clientHighlightSelectedStack != null ? (displayName + " (\u00a7e" + clientHighlightSelectedStack.getDisplayName() + "\u00a7r)") : displayName + " (\u00a7eNone\u00a7r)";
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(ItemModelMesher mesher) {
        mesher.register(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    public ItemStack getActiveStack(ItemStack itemStack) {
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if(tagCompound == null) {
            return null;
        }
        int selectedIndex = tagCompound.getByte("SelectedIndex");
        if(selectedIndex <= 0) {
            return null;
        }
        selectedIndex--;
        NBTTagList tagList = tagCompound.getTagList("ItemList", Constants.NBT.TAG_COMPOUND);
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
        NBTTagList tagList = tagCompound.getTagList("ItemList", Constants.NBT.TAG_COMPOUND);
        if(wrenchStack.stackSize <= 0) {
            tagList.removeTag(selectedIndex);
        } else {
            if (selectedIndex < tagList.tagCount()) {
                NBTTagCompound itemCompound = new NBTTagCompound();
                wrenchStack.writeToNBT(itemCompound);
                tagList.set(selectedIndex, itemCompound);
            }
        }
        tagCompound.setTag("ItemList", tagList);
        clientHeldSelectedStack = wrenchStack;
    }

    public IInventory getInventory(EntityPlayer entityPlayer) {
        return new ToolboxInventory(entityPlayer);
    }

    public static class ToolboxInventory implements IInventory {

        private final EntityPlayer entityPlayer;
        private final ItemStack[] inventory = new ItemStack[5];

        public ToolboxInventory(EntityPlayer entityPlayer) {
            this.entityPlayer = entityPlayer;
            ItemStack parentStack = entityPlayer.getHeldItem();
            if (!parentStack.hasTagCompound()) {
                parentStack.setTagCompound(new NBTTagCompound());
            }
            NBTTagList tagList = parentStack.getTagCompound().getTagList("ItemList", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound itemCompound = tagList.getCompoundTagAt(i);
                int slot = itemCompound.getByte("Slot");
                if (slot >= 0 && slot < inventory.length) {
                    inventory[slot] = ItemStack.loadItemStackFromNBT(itemCompound);
                }
            }
        }

        @Override
        public int getSizeInventory() {
            return inventory.length;
        }

        @Override
        public ItemStack getStackInSlot(int index) {
            return inventory[index];
        }

        @Override
        public ItemStack decrStackSize(int index, int count) {
            if (inventory[index] != null) {
                if (inventory[index].stackSize <= count) {
                    ItemStack itemStack = inventory[index];
                    inventory[index] = null;
                    markDirty();
                    return itemStack;
                } else {
                    ItemStack itemStack = inventory[index].splitStack(count);
                    if (inventory[index].stackSize == 0) {
                        inventory[index] = null;
                    }
                    markDirty();
                    return itemStack;
                }
            }
            return null;
        }

        @Override
        public void setInventorySlotContents(int index, ItemStack itemStack) {
            inventory[index] = itemStack;
            markDirty();
        }

        @Override
        public void markDirty() {

        }

        @Override
        public boolean isItemValidForSlot(int index, ItemStack itemStack) {
//            return toolboxRegistry.contains(itemStack.getItem());
            return true;
        }

        @Override
        public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
            return entityPlayer.getHeldItem() != null && entityPlayer.getHeldItem().getItem() == ModItems.toolbox;
        }

        @Override
        public ItemStack removeStackFromSlot(int index) {
            return null;
        }

        @Override
        public int getInventoryStackLimit() {
            return 64;
        }

        @Override
        public void openInventory(EntityPlayer player) {}

        @Override
        public void closeInventory(EntityPlayer player) {
            NBTTagList tagList = new NBTTagList();
            for (int i = 0; i < inventory.length; i++) {
                if (inventory[i] != null) {
                    NBTTagCompound itemCompound = new NBTTagCompound();
                    itemCompound.setByte("Slot", (byte) i);
                    inventory[i].writeToNBT(itemCompound);
                    tagList.appendTag(itemCompound);
                }
            }
            ItemStack parentStack = entityPlayer.getHeldItem();
            parentStack.getTagCompound().setTag("ItemList", tagList);
        }

        @Override
        public void clear() {}

        @Override
        public boolean hasCustomName() {
            return false;
        }

        @Override
        public String getName() {
            return "container." + RefinedRelocation2.MOD_ID + ":toolbox";
        }

        @Override
        public IChatComponent getDisplayName() {
            return new ChatComponentTranslation(getName());
        }

        @Override
        public int getField(int id) {
            return 0;
        }

        @Override
        public void setField(int id, int value) {}

        @Override
        public int getFieldCount() {
            return 0;
        }
    }

}
