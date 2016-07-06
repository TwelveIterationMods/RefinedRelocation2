package net.blay09.mods.refinedrelocation.filter;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.MutableClassToInstanceMap;
import net.blay09.mods.refinedrelocation.api.filter.IFilter;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class FilterRegistry {

	// TODO Creative Tab Filter
	// TODO Mod Filter

	private static final Map<String, Class<? extends IFilter>> filterMap = Maps.newHashMap();
	private static final List<Class<? extends IFilter>> filterList = Lists.newArrayList();
	private static final ClassToInstanceMap<IFilter> defaultInstances = MutableClassToInstanceMap.create();

	public static void registerFilter(Class<? extends IFilter> filterClass) {
		try {
			IFilter filter = filterClass.newInstance();
			if(filterMap.containsKey(filter.getIdentifier())) {
				throw new IllegalArgumentException("Filter with id '" + filter.getIdentifier() + "' is already registered.");
			}
			filterMap.put(filter.getIdentifier(), filterClass);
			filterList.add(filterClass);
			defaultInstances.put(filterClass, filter);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Could not instantiate new filter of class '" + filterClass.getSimpleName() + "': is a public no-arg constructor available?", e);
		}
	}

	@Nullable
	public static IFilter createFilter(String id) {
		Class<? extends IFilter> filterClass = filterMap.get(id);
		if(filterClass == null) {
			return null;
		}
		try {
			return filterClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Could not instantiate new filter of id '" + id + "': is a public no-arg constructor available?", e);
		}
	}

	@Deprecated
	@Nullable
	public static IFilter getFilterInstance(String id) {
		Class<? extends IFilter> filterClass = filterMap.get(id);
		if(filterClass == null) {
			return null;
		}
		return defaultInstances.get(filterClass);
	}

	@Nullable
	public static IFilter getFilterInstance(int index) {
		if(index < 0 || index >= filterList.size()) {
			return null;
		}
		Class<? extends IFilter> filterClass = filterList.get(index);
		return defaultInstances.get(filterClass);
	}

	public static int getFilterCount() {
		return filterMap.size();
	}

}
