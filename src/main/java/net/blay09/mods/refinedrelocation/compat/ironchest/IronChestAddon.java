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
    private Class<?> chestUpgradeItem;
    private Field lidAngleField;
    private Field chestUpgradeType;

    public IronChestAddon() {
        MinecraftForge.EVENT_BUS.register(this);

        try {
            ironChestTileEntity = Class.forName("com.progwml6.ironchest.common.block.tileentity.GenericIronChestTileEntity");
            lidAngleField = ironChestTileEntity.getDeclaredField("lidAngle");
            lidAngleField.setAccessible(true);

            chestUpgradeItem = Class.forName("com.progwml6.ironchest.common.item.ChestUpgradeItem");
            chestUpgradeType = chestUpgradeItem.getDeclaredField("type");
            chestUpgradeType.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            logger.error("Could not setup IronChests compat - some features may not work as expected!", e);
        }
    }

    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (chestUpgradeType == null || chestUpgradeItem == null) {
            logger.error("Could not upgrade sorting chest because IronChest compat did not setup correctly");
            return;
        }

        ItemStack itemStack = event.getItemStack();
        if (event.getWorld().isRemote || itemStack.isEmpty() || !(chestUpgradeItem.isAssignableFrom(itemStack.getItem().getClass()))) {
            return;
        }

        String upgradeName;
        try {
            upgradeName = ((Enum<?>) chestUpgradeType.get(itemStack.getItem())).name();
        } catch (IllegalAccessException e) {
            logger.error("Could not upgrade sorting chest because IronChest compat did not setup correctly", e);
            return;
        }

        SortingChestType sourceType;
        SortingChestType targetType;
        switch (upgradeName) {
            case "WOOD_TO_IRON":
                sourceType = SortingChestType.WOOD;
                targetType = SortingChestType.IRON;
                break;
            case "IRON_TO_GOLD":
                sourceType = SortingChestType.IRON;
                targetType = SortingChestType.GOLD;
                break;
            case "GOLD_TO_DIAMOND":
                sourceType = SortingChestType.GOLD;
                targetType = SortingChestType.DIAMOND;
                break;
            default:
                return;
        }

        World world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof SortingChestBlock)) {
            return;
        }

        if (((SortingChestBlock) state.getBlock()).getChestType() != sourceType) {
            return;
        }

        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof SortingChestTileEntity)) {
            return;
        }

        SortingChestTileEntity tileSortingChest = (SortingChestTileEntity) tileEntity;
        if (tileSortingChest.getNumPlayersUsing() > 0) {
            return;
        }

        Direction facing = state.get(SortingChestBlock.FACING);
        CompoundNBT serialized = tileEntity.write(new CompoundNBT());
        IClearable.clearObj(tileEntity);
        BlockState newState = ModBlocks.sortingChests[targetType.ordinal()].getDefaultState().with(SortingChestBlock.FACING, facing);
        world.setBlockState(pos, newState);

        TileEntity newTileEntity = world.getTileEntity(pos);
        if (newTileEntity instanceof SortingChestTileEntity) {
            newTileEntity.read(serialized);
        }

        if (!event.getPlayer().abilities.isCreativeMode) {
            itemStack.shrink(1);
        }

        event.setCanceled(true);
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
            switch (registryName) {
                case "ironchest:iron_chest":
                    chestType = SortingChestType.IRON;
                    break;
                case "ironchest:gold_chest":
                    chestType = SortingChestType.GOLD;
                    break;
                case "ironchest:diamond_chest":
                    chestType = SortingChestType.DIAMOND;
                    break;
                default:
                    return false;
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
