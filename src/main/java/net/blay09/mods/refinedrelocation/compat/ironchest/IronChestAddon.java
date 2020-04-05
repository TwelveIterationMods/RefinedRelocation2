package net.blay09.mods.refinedrelocation.compat.ironchest;

import net.blay09.mods.refinedrelocation.ModBlocks;
import net.blay09.mods.refinedrelocation.RefinedRelocation;
import net.blay09.mods.refinedrelocation.SortingChestType;
import net.blay09.mods.refinedrelocation.api.Capabilities;
import net.blay09.mods.refinedrelocation.api.ISortingUpgradable;
import net.blay09.mods.refinedrelocation.block.SortingChestBlock;
import net.blay09.mods.refinedrelocation.tile.SortingChestTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IClearable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Objects;

public class IronChestAddon {

    private static final Logger logger = LogManager.getLogger();

    private Class<?> ironChestTileEntity;
    private Field lidAngleField;

    public IronChestAddon() {
        MinecraftForge.EVENT_BUS.register(this);

        try {
            ironChestTileEntity = Class.forName("com.progwml6.ironchest.common.block.tileentity.GenericIronChestTileEntity");
            lidAngleField = ironChestTileEntity.getDeclaredField("lidAngle");
            lidAngleField.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            logger.error("Could not setup IronChests compat - some features may not work as expected!", e);
        }
    }

    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        /*if (event.getWorld().isRemote || event.getItemStack().isEmpty() || !(event.getItemStack().getItem() instanceof ChestUpgradeItem)) {
            return;
        }

        World world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        IronChestsUpgradeType type = IronChestsUpgradeType.WOOD_TO_IRON; // ((ChestUpgradeItem) event.getItemStack().getItem()); // TODO reflection needed
        if (type.canUpgrade(IronChestsTypes.WOOD)) {
            if (state.getBlock() != ModBlocks.sortingChest) {
                return;
            }
        } else {
            if (state != sortingIronChest.getStateFromMeta(IronChestsTypes.valueOf(type.source.getName().toUpperCase()).ordinal())) {
                return;
            }
        }

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity == null) {
            return;
        }
        NonNullList<ItemStack> previousContent;
        Direction previousFacing;
        IRootFilter rootFilter = tileEntity.getCapability(Capabilities.ROOT_FILTER);
        ISortingInventory sortingInventory = tileEntity.getCapability(Capabilities.SORTING_INVENTORY);
        if (tileEntity instanceof SortingIronChestTileEntity) {
            previousContent = ((SortingIronChestTileEntity) tileEntity).getItems();
            previousFacing = ((SortingIronChestTileEntity) tileEntity).getFacing();
        } else if (tileEntity instanceof SortingChestTileEntity) {
            previousFacing = state.get(ChestBlock.FACING);
            SortingChestTileEntity tileSortingChest = (SortingChestTileEntity) tileEntity;
            if (tileSortingChest.getNumPlayersUsing() > 0) {
                return;
            }
            previousContent = NonNullList.create();
            for (int i = 0; i < tileSortingChest.getItemHandler().getSlots(); i++) {
                previousContent.add(tileSortingChest.getItemHandler().getStackInSlot(i));
            }
        } else {
            return;
        }

        tileEntity.updateContainingBlockInfo();

        world.removeTileEntity(pos);
        world.removeBlock(pos);

        BlockState newState = Compat.sortingIronChest.getDefaultState().with(BlockIronChest.VARIANT_PROP, type.target);
        world.setBlockState(pos, newState, 3);

        TileEntity newTileEntity = world.getTileEntity(pos);
        if (newTileEntity instanceof SortingIronChestTileEntity) {
            ((SortingIronChestTileEntity) newTileEntity).setContents(previousContent);
            ((SortingIronChestTileEntity) newTileEntity).setFacing(previousFacing);
            IRootFilter newRootFilter = newTileEntity.getCapability(Capabilities.ROOT_FILTER, null);
            if (rootFilter != null && newRootFilter != null) {
                newRootFilter.deserializeNBT(rootFilter.serializeNBT());
            }
            ISortingInventory newSortingInventory = newTileEntity.getCapability(Capabilities.SORTING_INVENTORY, null);
            if (sortingInventory != null && newSortingInventory != null) {
                newSortingInventory.deserializeNBT(sortingInventory.serializeNBT());
            }
        }

        if (!event.getPlayer().abilities.isCreativeMode) {
            event.getItemStack().shrink(1);
        }
        event.setCanceled(true);*/
    }

    private class IronChestCapabilityProvider implements ICapabilityProvider, ISortingUpgradable {
        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return Capabilities.SORTING_UPGRADABLE.orEmpty(cap, LazyOptional.of(() -> this));
        }

        @Override
        public boolean applySortingUpgrade(TileEntity tileEntity, ItemStack itemStack, PlayerEntity player, World world, BlockPos pos, Direction side, double hitX, double hitY, double hitZ, Hand hand) {
            try {
                if (lidAngleField == null || lidAngleField.getFloat(tileEntity) > 0) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                logger.error("Failed to upgrade chest due to incompatibility", e);
                return false;
            }

            BlockState state = world.getBlockState(pos);
            String registryName = Objects.toString(state.getBlock().getRegistryName());
            SortingChestType chestType;
            switch(registryName) {
                case "ironchest:iron_chest":
                    chestType = SortingChestType.IRON;
                    break;
                case "ironchest:gold_chest":
                    chestType = SortingChestType.GOLD;
                    break;
                case "ironchest:diamond_chest":
                    chestType = SortingChestType.DIAMOND;
                    break;
                default: return false;
            }
            CompoundNBT storedData = tileEntity.write(new CompoundNBT());
            Direction facing = state.get(HorizontalBlock.HORIZONTAL_FACING);
            SortingChestBlock sortingChestBlock = ModBlocks.sortingChests[chestType.ordinal()];
            IClearable.clearObj(tileEntity);
            world.setBlockState(pos, sortingChestBlock.getDefaultState().with(SortingChestBlock.FACING, facing));
            SortingChestTileEntity sortingChest = (SortingChestTileEntity) world.getTileEntity(pos);
            if (sortingChest != null) {
                sortingChest.restoreItems(storedData.getList("Items", Constants.NBT.TAG_COMPOUND));
            }
            return true;
        }
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<TileEntity> event) {
        if (ironChestTileEntity != null && ironChestTileEntity.isAssignableFrom(event.getObject().getClass())) {
            event.addCapability(new ResourceLocation(RefinedRelocation.MOD_ID, "sorting_upgradable"), new IronChestCapabilityProvider());
        }
    }

}
