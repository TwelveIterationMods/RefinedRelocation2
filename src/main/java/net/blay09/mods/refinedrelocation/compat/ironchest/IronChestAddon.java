//package net.blay09.mods.refinedrelocation.compat.ironchest;
//
//import com.google.common.collect.Sets;
//import com.progwml6.ironchest.client.screen.IronChestScreen;
//import com.progwml6.ironchest.common.block.IronChestsTypes;
//import com.progwml6.ironchest.common.block.tileentity.GenericIronChestTileEntity;
//import net.blay09.mods.refinedrelocation.RefinedRelocation;
//import net.blay09.mods.refinedrelocation.api.Capabilities;
//import net.blay09.mods.refinedrelocation.api.ISortingUpgradable;
//import net.blay09.mods.refinedrelocation.api.RefinedRelocationAPI;
//import net.blay09.mods.refinedrelocation.compat.Compat;
//import net.blay09.mods.refinedrelocation.compat.RefinedRelocationAddon;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.client.gui.screen.inventory.ContainerScreen;
//import net.minecraft.client.gui.widget.button.Button;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.inventory.IInventory;
//import net.minecraft.item.BlockItem;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.tileentity.TileEntityType;
//import net.minecraft.util.Direction;
//import net.minecraft.util.Hand;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.client.event.GuiScreenEvent;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.capabilities.ICapabilityProvider;
//import net.minecraftforge.common.util.LazyOptional;
//import net.minecraftforge.event.AttachCapabilitiesEvent;
//import net.minecraftforge.event.entity.player.PlayerInteractEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.client.registry.ClientRegistry;
//import net.minecraftforge.registries.IForgeRegistry;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//
//public class IronChestAddon implements RefinedRelocationAddon {
//
//    public static Block sortingIronChest;
//    public static TileEntityType<SortingIronChestTileEntity> sortingIronChestTile;
//
//    @Override
//    public void registerTileEntities(IForgeRegistry<TileEntityType<?>> registry) {
//        registry.registerAll(
//                sortingIronChestTile = new TileEntityType<>(SortingIronChestTileEntity.Iron::new, Sets.newHashSet(sortingIronChest), null)
//        );
////		GameRegistry.registerTileEntity(TileSortingIronChest.Dirt.class, BlockSortingIronChest.registryName.toString() + "_dirt");
////		GameRegistry.registerTileEntity(TileSortingIronChest.Obsidian.class, BlockSortingIronChest.registryName.toString() + "_obsidian");
////		GameRegistry.registerTileEntity(TileSortingIronChest.Crystal.class, BlockSortingIronChest.registryName.toString() + "_crystal");
////		GameRegistry.registerTileEntity(TileSortingIronChest.Diamond.class, BlockSortingIronChest.registryName.toString() + "_diamond");
////		GameRegistry.registerTileEntity(TileSortingIronChest.Copper.class, BlockSortingIronChest.registryName.toString() + "_copper");
////		GameRegistry.registerTileEntity(TileSortingIronChest.Gold.class, BlockSortingIronChest.registryName.toString() + "_gold");
////		GameRegistry.registerTileEntity(TileSortingIronChest.Silver.class, BlockSortingIronChest.registryName.toString() + "_silver");
//    }
//
//    @Override
//    public void preInit() {
//		/*final Object modInstance = ModList.get().getModObjectById(Compat.IRON_CHEST);
//		final ITileGuiHandler tileGuiHandler = (player, tileEntity) -> {
//			TileSortingIronChest tileIronChest = (TileSortingIronChest) tileEntity;
//			player.openGui(modInstance, tileIronChest.getType().ordinal(), tileEntity.getWorld(), tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ());
//		};
//
//		RefinedRelocationAPI.registerGuiHandler(TileSortingIronChest.class, tileGuiHandler);
//		RefinedRelocationAPI.registerGuiHandler(TileSortingIronChest.Dirt.class, tileGuiHandler);
//		RefinedRelocationAPI.registerGuiHandler(TileSortingIronChest.Obsidian.class, tileGuiHandler);
//		RefinedRelocationAPI.registerGuiHandler(TileSortingIronChest.Crystal.class, tileGuiHandler);
//		RefinedRelocationAPI.registerGuiHandler(TileSortingIronChest.Diamond.class, tileGuiHandler);
//		RefinedRelocationAPI.registerGuiHandler(TileSortingIronChest.Copper.class, tileGuiHandler);
//		RefinedRelocationAPI.registerGuiHandler(TileSortingIronChest.Gold.class, tileGuiHandler);
//		RefinedRelocationAPI.registerGuiHandler(TileSortingIronChest.Silver.class, tileGuiHandler);*/
//
//        MinecraftForge.EVENT_BUS.register(this);
//    }
//
//    @Override
//    public void registerBlocks(IForgeRegistry<Block> registry) {
//        registry.registerAll(
//                sortingIronChest = new BlockSortingIronChest(IronChestsTypes.IRON).setRegistryName(BlockSortingIronChest.registryName)
//        );
//    }
//
//    @Override
//    public void registerItems(IForgeRegistry<Item> registry) {
//        registry.registerAll(
//                new BlockItem(sortingIronChest, new Item.Properties()).setRegistryName(BlockSortingIronChest.registryName)
//        );
//    }
//
//    @Override
//    public void setupClient() {
//        ClientRegistry.bindTileEntityRenderer(sortingIronChestTile, RenderSortingIronChest::new);
//    }
//
//    @Override
//    public void init() {
//        ((BlockSortingIronChest) sortingIronChest).setDelegateBlock(Compat.ironChest);
//    }
//
//    @SubscribeEvent
//    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
//        /*if (event.getWorld().isRemote || event.getItemStack().isEmpty() || !(event.getItemStack().getItem() instanceof ChestUpgradeItem)) {
//            return;
//        }
//
//        World world = event.getWorld();
//        BlockPos pos = event.getPos();
//        BlockState state = world.getBlockState(pos);
//        IronChestsUpgradeType type = IronChestsUpgradeType.WOOD_TO_IRON; // ((ChestUpgradeItem) event.getItemStack().getItem()); // TODO reflection needed
//        if (type.canUpgrade(IronChestsTypes.WOOD)) {
//            if (state.getBlock() != ModBlocks.sortingChest) {
//                return;
//            }
//        } else {
//            if (state != sortingIronChest.getStateFromMeta(IronChestsTypes.valueOf(type.source.getName().toUpperCase()).ordinal())) {
//                return;
//            }
//        }
//
//        TileEntity tileEntity = world.getTileEntity(pos);
//        if (tileEntity == null) {
//            return;
//        }
//        NonNullList<ItemStack> previousContent;
//        Direction previousFacing;
//        IRootFilter rootFilter = tileEntity.getCapability(Capabilities.ROOT_FILTER);
//        ISortingInventory sortingInventory = tileEntity.getCapability(Capabilities.SORTING_INVENTORY);
//        if (tileEntity instanceof SortingIronChestTileEntity) {
//            previousContent = ((SortingIronChestTileEntity) tileEntity).getItems();
//            previousFacing = ((SortingIronChestTileEntity) tileEntity).getFacing();
//        } else if (tileEntity instanceof SortingChestTileEntity) {
//            previousFacing = state.get(ChestBlock.FACING);
//            SortingChestTileEntity tileSortingChest = (SortingChestTileEntity) tileEntity;
//            if (tileSortingChest.getNumPlayersUsing() > 0) {
//                return;
//            }
//            previousContent = NonNullList.create();
//            for (int i = 0; i < tileSortingChest.getItemHandler().getSlots(); i++) {
//                previousContent.add(tileSortingChest.getItemHandler().getStackInSlot(i));
//            }
//        } else {
//            return;
//        }
//
//        tileEntity.updateContainingBlockInfo();
//
//        world.removeTileEntity(pos);
//        world.removeBlock(pos);
//
//        BlockState newState = Compat.sortingIronChest.getDefaultState().with(BlockIronChest.VARIANT_PROP, type.target);
//        world.setBlockState(pos, newState, 3);
//
//        TileEntity newTileEntity = world.getTileEntity(pos);
//        if (newTileEntity instanceof SortingIronChestTileEntity) {
//            ((SortingIronChestTileEntity) newTileEntity).setContents(previousContent);
//            ((SortingIronChestTileEntity) newTileEntity).setFacing(previousFacing);
//            IRootFilter newRootFilter = newTileEntity.getCapability(Capabilities.ROOT_FILTER, null);
//            if (rootFilter != null && newRootFilter != null) {
//                newRootFilter.deserializeNBT(rootFilter.serializeNBT());
//            }
//            ISortingInventory newSortingInventory = newTileEntity.getCapability(Capabilities.SORTING_INVENTORY, null);
//            if (sortingInventory != null && newSortingInventory != null) {
//                newSortingInventory.deserializeNBT(sortingInventory.serializeNBT());
//            }
//        }
//
//        if (!event.getPlayer().abilities.isCreativeMode) {
//            event.getItemStack().shrink(1);
//        }
//        event.setCanceled(true);*/
//    }
//
//    private class IronChestCapabilityProvider implements ICapabilityProvider, ISortingUpgradable {
//        @Nonnull
//        @Override
//        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
//            return Capabilities.SORTING_UPGRADABLE.orEmpty(cap, LazyOptional.of(() -> this));
//        }
//
//        @Override
//        public boolean applySortingUpgrade(TileEntity tileEntity, ItemStack itemStack, PlayerEntity player, World world, BlockPos pos, Direction side, double hitX, double hitY, double hitZ, Hand hand) {
//            GenericIronChestTileEntity tileIronChest = (GenericIronChestTileEntity) tileEntity;
//            if (tileIronChest.getLidAngle(0f) > 0) {
//                return false;
//            }
//
//            CompoundNBT storedData = tileIronChest.write(new CompoundNBT());
//            tileIronChest.clear();
//            BlockState oldState = world.getBlockState(pos);
//            /*world.setBlockState(pos, sortingIronChest.getDefaultState()
//                    .with(IronChestBlock.VARIANT_PROP, oldState.get(IronChestBlock.VARIANT_PROP)));*/
//            SortingIronChestTileEntity sortingIronChest = (SortingIronChestTileEntity) world.getTileEntity(pos);
//            if (sortingIronChest == null) {
//                return false;
//            }
//            sortingIronChest.read(storedData);
//            return true;
//        }
//    }
//
//    @SubscribeEvent
//    public void attachCapabilities(AttachCapabilitiesEvent<TileEntity> event) {
//        if (event.getObject() instanceof GenericIronChestTileEntity) {
//            event.addCapability(new ResourceLocation(RefinedRelocation.MOD_ID, "SortingUpgradable"), new IronChestCapabilityProvider());
//        }
//    }
//
//    @SubscribeEvent
//    @OnlyIn(Dist.CLIENT)
//    public void onInitGui(GuiScreenEvent.InitGuiEvent event) {
//        if (event.getGui() instanceof IronChestScreen) {
//            ContainerScreen<?> guiContainer = (ContainerScreen<?>) event.getGui();
//            IInventory tileIronChest = guiContainer.getContainer().inventorySlots.get(0).inventory;
//            if (tileIronChest instanceof SortingIronChestTileEntity) {
//                Button button = RefinedRelocationAPI.createOpenFilterButton(guiContainer, (TileEntity) tileIronChest, -1);
//                button.x += 7;
//                button.y += 3;
//                event.addWidget(button);
//            }
//        }
//    }
//}
