package net.blay09.mods.refinedrelocation.tile;

import net.blay09.mods.refinedrelocation.ModTileEntities;
import net.blay09.mods.refinedrelocation.block.FastHopperBlock;
import net.blay09.mods.refinedrelocation.container.ContainerFastHopper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.INameable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Predicate;

public class FastHopperTileEntity extends TileMod implements ITickableTileEntity, INamedContainerProvider, INameable {

    private static final Predicate<? super Entity> HAS_ITEM_HANDLER = (entity) -> entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent();

    private final ItemStackHandler itemHandler = createItemHandler();

    private ITextComponent customName;
    private int cooldown;

    public FastHopperTileEntity() {
        super(ModTileEntities.fastHopper);
    }

    public FastHopperTileEntity(TileEntityType<?> type) {
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
                BlockState state = world.getBlockState(getPos());
                boolean isEnabled = state.get(FastHopperBlock.ENABLED);
                if (!isEnabled) {
                    return;
                }

                Direction facing = state.get(FastHopperBlock.FACING);
                Direction opposite = facing.getOpposite();
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
                    LazyOptional<IItemHandler> itemHandlerCap = getItemHandlerAt(pos.offset(Direction.UP));
                    if (itemHandlerCap.isPresent()) {
                        pullItem(itemHandlerCap.orElseThrow(ConcurrentModificationException::new));
                    }
                }

                cooldown = 20;
            }
        }
    }

    private LazyOptional<IItemHandler> getItemHandlerAt(BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null) {
            return tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN);
        }

        List<Entity> list = world.getEntitiesInAABBexcluding(null, new AxisAlignedBB(pos.getX() - 0.5, pos.getY() - 0.5, pos.getZ() - 0.5, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), EntityPredicates.HAS_INVENTORY);
        if (!list.isEmpty()) {
            Entity entity = list.get(world.rand.nextInt(list.size()));
            return LazyOptional.of(() -> new InvWrapper(((IInventory) entity)));
        }

        return LazyOptional.empty();
    }

    private void pushItem(int sourceSlot, IItemHandler targetItemHandler) {
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

    private boolean pullItem(ItemEntity entityItem) {
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
    public CompoundNBT write(CompoundNBT tagCompound) {
        super.write(tagCompound);
        tagCompound.put("ItemHandler", itemHandler.serializeNBT());
        if (customName != null) {
            tagCompound.putString("CustomName", customName.getUnformattedComponentText());
        }

        return tagCompound;
    }

    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
        itemHandler.deserializeNBT(tagCompound.getCompound("ItemHandler"));
        customName = tagCompound.contains("CustomName") ? new StringTextComponent(tagCompound.getString("CustomName")) : null;
    }

    @Override
    public String getUnlocalizedName() {
        return "container.refinedrelocation:fast_hopper";
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(getUnlocalizedName());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        LazyOptional<T> result = super.getCapability(cap, side);
        if (!result.isPresent()) {
            result = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> itemHandler));
        }

        return result;
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity player) {
        return new ContainerFastHopper(i, playerInventory, this);
    }

    @Override
    public ITextComponent getName() {
        return customName != null ? customName : new TranslationTextComponent(getUnlocalizedName());
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

    public void onEntityCollision(Entity entity) {
        if (entity instanceof ItemEntity) {
            if (VoxelShapes.compare(VoxelShapes.create(entity.getBoundingBox().offset(-pos.getX(), -pos.getY(), -pos.getZ())), IHopper.COLLECTION_AREA_SHAPE, IBooleanFunction.AND)) {
                pullItem((ItemEntity) entity);
            }
        }
    }
}
