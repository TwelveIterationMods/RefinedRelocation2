package net.blay09.mods.refinedrelocation.filter;

import com.google.common.collect.Maps;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;

import java.util.Map;

public class FilterRegistry {

	private static final Map<String, Class<? extends IFilter>> filterMap = Maps.newHashMap();

	public static void registerFilter(String id, Class<? extends IFilter> filterClass) {
		if(filterMap.containsKey(id)) {
			throw new IllegalArgumentException("Filter with id '" + id + "' is already registered.");
		} else if(filterClass == null) {
			throw new NullPointerException("Registered Filter class must not be null.");
		}
		filterMap.put(id, filterClass);
	}

	public static IFilter createFilter(String id) {
		Class<? extends IFilter> filterClass = filterMap.get(id);
		if(filterClass == null) {
			// TODO this should not crash, otherwise removing mods that add filters will be problematic; instead just remove the filter from the parent list
			throw new IllegalArgumentException("Filter with id '" + id + "' is not registered.");
		}
		try {
			return filterClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Could not instantiate new filter of id '" + id + "': is a public no-arg constructor available?", e);
		}
	}

}
