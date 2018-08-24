# Refined Relocation 2

Minecraft Mod. Adds sorting networks, filter systems, block extenders and more.

[![Versions](http://cf.way2muchnoise.eu/versions/refined-relocation-2.svg)](https://minecraft.curseforge.com/projects/refined-relocation-2) [![Downloads](http://cf.way2muchnoise.eu/full_refined-relocation-2_downloads.svg)](https://minecraft.curseforge.com/projects/refined-relocation-2)

# Development Builds
Potentially unstable in-development releases built straight from the latest code in this repository are available on my [Jenkins](http://jenkins.blay09.net).
They may contain unfinished and broken features, so run them at your own risk.

## API

Refined Relocation's API is still in development, so if you run into any issues or have suggestions on things that could go smoother, let me know.

You can add Refined Relocation to your working environment using the Curse Maven.

1. Register Refined Relocation's maven repository by adding the following lines to your _build.gradle_:

```
repositories {
    maven {
        url "https://minecraft.curseforge.com/api/maven/"
    }
    
    dependencies {
        deobfCompile 'refined-relocation-2:RefinedRelocation_1.12.2:4.2.28' // make sure to look up the latest version from the CurseForge files page here
    }
}
```

2. Run `gradlew` to update your project and you'll be good to go. 
If you'd like to have Refined Relocation's API available, but not have the mod loaded, 
change the `deobfCompile` to `provided`.

### Adding a Sorting Chest
In order for a chest to be considered a sorting chest, it must implement the `ISortingInventory` and `IRootfilter` capabilities.
```java
private final ISortingInventory sortingInventory = Capabilities.getDefaultInstance(Capabilities.SORTING_INVENTORY);
private final IRootFilter rootFilter = Capabilities.getDefaultInstance(Capabilities.ROOT_FILTER);
```

* If your tile should act as a member of the sorting grid, but does not expose an inventory of its own to the network, you can use `ISortingGridMember` instead of `ISortingInventory`.
* If your tile has a simple non-configurable filter (e.g. a barrel with simple item match), you use implement `ISimpleFilter` instead of `IRootFilter`.

To support the `ISortingGridMember` capability, you must delegate `onLoad()`, `invalidate()` and `onChunkUnload()` to the capability.
```java
@Override
public void onLoad() {
    super.onLoad();
    sortingInventory.onLoad(this);
}

@Override
public void invalidate() {
    super.invalidate();
    sortingInventory.onInvalidate(this);
}

@Override
public void onChunkUnload() {
    super.onChunkUnload();
    sortingInventory.onInvalidate(this);
}
```

For `ISortingInventory`, the tile entity must implement `ITickable` and delegate `update()` to the capability.
```java
@Override
public void update() {
    sortingInventory.onUpdate(this);
}
```

Finally, you have to implement `hasCapability()` and `getCapability()` accordingly.

```java
@Override
public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
     return Capabilities.isSortingGridCapability(capability)
             || Capabilities.isFilterCapability(capability)
             || super.hasCapability(capability, facing);
}

@Override
@SuppressWarnings("unchecked")
public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    if (Capabilities.isSortingGridCapability(capability)) {
        return (T) sortingInventory;
    } else if (Capabilities.isFilterCapability(capability)) {
        return (T) rootFilter;
    }

    return super.getCapability(capability, facing);
}
```

### Making your chest upgradable to a sorting chest
To allow the Sorting Upgrade item to work on your normal chest, it must support the `ISortingUpgradable` capability.
This capability does not have a default implementation, you must provide the conversion code yourself.

Check the Iron Chest compat for example:
```java
@Override
public boolean applySortingUpgrade(TileEntity tileEntity, ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
    TileEntityIronChest tileIronChest = (TileEntityIronChest) tileEntity;
    // Do not upgrade if the chest is open
    if (tileIronChest.lidAngle > 0) {
        return false;
    }
    
    // Save the chest's data to NBT, then delete all items inside to prevent them from dropping when replacing the block
    NBTTagCompound storedData = tileIronChest.writeToNBT(new NBTTagCompound());
    tileIronChest.clear();
    
    // Convert the old block state to the new sorting version of the block state
    IBlockState oldState = world.getBlockState(pos);
    world.setBlockState(pos, Compat.sortingIronChest.getDefaultState()
            .withProperty(BlockIronChest.VARIANT_PROP, oldState.getValue(BlockIronChest.VARIANT_PROP)));
    
    // Retrieve the newly created sorting tile entity and have it load the previously saved NBT
    TileSortingIronChest sortingIronChest = (TileSortingIronChest) world.getTileEntity(pos);
    if (sortingIronChest == null) {
        return false;
    }
    
    sortingIronChest.readFromNBT(storedData);
    return true;
}
```

## Useful Links
* [Latest Builds](http://jenkins.blay09.net/) on my Jenkins
* [@BlayTheNinth](https://twitter.com/BlayTheNinth) on Twitter