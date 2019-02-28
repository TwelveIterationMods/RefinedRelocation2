package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.ModTiles;
import net.blay09.mods.refinedrelocation.block.BlockFastHopper;
import net.blay09.mods.refinedrelocation.container.ContainerFastHopper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ConcurrentModificationException;

public class TileFastHopper extends TileMod implements ITickable, IInteractionObject {

    private final ItemStackHandler itemHandler = createItemHandler();

    private ITextComponent customName;
    private int cooldown;

    public TileFastHopper() {
        super(ModTiles.fastHopper);
    }

    public TileFastHopper(TileEntityType<?> type) {
        super(type);
    }

    protected ItemStackHandler createItemHandler() {
        return new ItemStackHandler(5) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }
        };
    }

    @Override
    public void tick() {
        if (world != null && !world.isRemote) {
            cooldown--;
            if (cooldown <= 0) {
                EnumFacing facing = world.getBlockState(getPos()).get(BlockFastHopper.FACING);
                EnumFacing opposite = facing.getOpposite();
                TileEntity facingTile = world.getTileEntity(pos.offset(facing));
                LazyOptional<IItemHandler> targetItemHandlerCap = facingTile != null ? facingTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, opposite) : LazyOptional.empty();
                boolean hasSpace = false;
                if (targetItemHandlerCap.isPresent()) {
                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                        ItemStack itemStack = itemHandler.getStackInSlot(i);
                        if (!itemStack.isEmpty()) {
                            pushItem(i, targetItemHandlerCap.orElseThrow(ConcurrentModificationException::new));
                        }
                        if (!hasSpace && (itemStack.isEmpty() || itemStack.getCount() < itemStack.getMaxStackSize())) {
                            hasSpace = true;
                        }
                    }
                } else {
                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                        if (itemHandler.getStackInSlot(i).isEmpty()) {
                            hasSpace = true;
                            break;
                        }
                    }
                }

                if (hasSpace) {
                    BlockPos upPos = pos.offset(EnumFacing.UP);
                    TileEntity upTile = world.getTileEntity(upPos);
                    LazyOptional<IItemHandler> itemHandlerCap = upTile != null ? upTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN) : LazyOptional.empty();
                    if (itemHandlerCap.isPresent()) {
                        pullItem(itemHandlerCap.orElseThrow(ConcurrentModificationException::new));
                    } else {
                        float range = 0.75f;
                        world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + 1.5f, pos.getZ() + range), EntitySelectors.IS_ALIVE).forEach(this::pullItem);
                    }
                }

                cooldown = 20;
            }
        }
    }

    public void pushItem(int sourceSlot, IItemHandler targetItemHandler) {
        ItemStack sourceStack = itemHandler.extractItem(sourceSlot, Items.AIR.getItemStackLimit(ItemStack.EMPTY), true);
        ItemStack restStack = ItemHandlerHelper.insertItem(targetItemHandler, sourceStack, false);
        itemHandler.extractItem(sourceSlot, restStack.isEmpty() ? sourceStack.getCount() : sourceStack.getCount() - restStack.getCount(), false);
    }

    private void pullItem(IItemHandler sourceItemHandler) {
        for (int i = 0; i < sourceItemHandler.getSlots(); i++) {
            ItemStack sourceStack = sourceItemHandler.extractItem(i, Items.AIR.getItemStackLimit(ItemStack.EMPTY), true);
            if (!sourceStack.isEmpty()) {
                ItemStack restStack = ItemHandlerHelper.insertItem(itemHandler, sourceStack, false);
                sourceItemHandler.extractItem(i, restStack.isEmpty() ? Items.AIR.getItemStackLimit(ItemStack.EMPTY) : sourceStack.getCount() - restStack.getCount(), false);
                break;
            }
        }
    }

    public boolean pullItem(EntityItem entityItem) {
        ItemStack sourceStack = entityItem.getItem();
        ItemStack restStack = ItemHandlerHelper.insertItem(itemHandler, sourceStack, false);
        if (!restStack.isEmpty()) {
            entityItem.setItem(restStack);
        } else {
            entityItem.remove();
        }
        return restStack.isEmpty() || restStack.getCount() < sourceStack.getCount();
    }

    @Override
    public NBTTagCompound write(NBTTagCompound tagCompound) {
        super.write(tagCompound);
        tagCompound.put("ItemHandler", itemHandler.serializeNBT());
        if (customName != null) {
            tagCompound.putString("CustomName", customName.getUnformattedComponentText());
        }

        return tagCompound;
    }

    @Override
    public void read(NBTTagCompound tagCompound) {
        super.read(tagCompound);
        itemHandler.deserializeNBT(tagCompound.getCompound("ItemHandler"));
        customName = tagCompound.contains("CustomName") ? new TextComponentString(tagCompound.getString("CustomName")) : null;
    }

    @Override
    public String getUnlocalizedName() {
        return "container.refinedrelocation:fast_hopper";
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> itemHandler));
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryPlayer, EntityPlayer entityPlayer) {
        return new ContainerFastHopper(entityPlayer, this);
    }

    @Override
    public String getGuiID() {
        return "refinedrelocation:fast_hopper";
    }

    @Override
    public ITextComponent getName() {
        return customName != null ? customName : new TextComponentTranslation(getUnlocalizedName());
    }

    @Override
    public boolean hasCustomName() {
        return customName != null;
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return customName;
    }
}
