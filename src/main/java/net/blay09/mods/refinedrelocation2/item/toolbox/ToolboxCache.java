package net.blay09.mods.refinedrelocation2.item.toolbox;

import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Map;

public class ToolboxCache {

    private static final int REFRESH_TICKS = 72000;
    private static final int LOWLIFE_TICKS = 1200;

    public static class ToolboxCacheEntry {
        public final ItemStack itemStack;
        public final ItemStack activeTool;

        public ToolboxCacheEntry(ItemStack itemStack, ItemStack activeTool) {
            this.itemStack = itemStack;
            this.activeTool = activeTool;
        }
    }

    private final Map<Entity, ToolboxCacheEntry> clientActiveToolCache = Maps.newHashMap();
    private final Map<Entity, ToolboxCacheEntry> serverActiveToolCache = Maps.newHashMap();
    private final Map<ItemStack, ItemStack> lowLifeCache = Maps.newHashMap();
    private int ticksSinceRefresh;
    private int ticksSinceLowlife;

    public ToolboxCache() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void refreshActiveStack(Entity entity, ItemStack itemStack) {
        ItemStack activeStack = loadActiveStack(itemStack);
        (entity.getEntityWorld().isRemote ? clientActiveToolCache : serverActiveToolCache).put(entity, new ToolboxCacheEntry(itemStack, activeStack));
        lowLifeCache.put(itemStack, activeStack);
    }

    public ItemStack getActiveStack(Entity entity, ItemStack itemStack) {
        if(!hasSelection(itemStack)) {
            return null;
        }
        ToolboxCacheEntry entry = entity.getEntityWorld().isRemote ? clientActiveToolCache.get(entity) : serverActiveToolCache.get(entity);
        if(entry != null) {
            ItemStack cachedStack = entry.activeTool;
            if (entry.itemStack == itemStack) {
                return cachedStack;
            }
        }
        ItemStack activeStack = loadActiveStack(itemStack);
        (entity.getEntityWorld().isRemote ? clientActiveToolCache : serverActiveToolCache).put(entity, new ToolboxCacheEntry(itemStack, activeStack));
        return activeStack;
    }

    public ItemStack getActiveStack(ItemStack itemStack) {
        if(!hasSelection(itemStack)) {
            return null;
        }
        ItemStack activeStack = lowLifeCache.get(itemStack);
        if(activeStack == null) {
            activeStack = loadActiveStack(itemStack);
            lowLifeCache.put(itemStack, activeStack);
        }
        return activeStack;
    }

    public ItemStack loadActiveStack(ItemStack itemStack) {
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

    private boolean hasSelection(ItemStack itemStack) {
        return itemStack.hasTagCompound() && itemStack.getTagCompound().getByte("SelectedIndex") > 0;
    }

    @SubscribeEvent
    public void onClearCache(TickEvent.WorldTickEvent event) {
        ticksSinceRefresh++;
        ticksSinceLowlife++;
        if(ticksSinceLowlife >= LOWLIFE_TICKS) {
            lowLifeCache.clear();
            ticksSinceLowlife = 0;
        }
        if(ticksSinceRefresh >= REFRESH_TICKS) {
            clientActiveToolCache.clear();
            serverActiveToolCache.clear();
            ticksSinceRefresh = 0;
        }
    }

}
