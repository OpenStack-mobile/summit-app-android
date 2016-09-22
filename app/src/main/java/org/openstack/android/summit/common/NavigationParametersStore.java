package org.openstack.android.summit.common;

import java.util.HashMap;

/**
 * Created by Claudio Redi on 1/19/2016.
 */
public class NavigationParametersStore implements INavigationParametersStore {
    private HashMap<String, Object> navigationParameterStore = new HashMap<>();

    @Override
    public <T> T pop(String key, Class<T> type) {
        Object value = null;
        if (navigationParameterStore.containsKey(key)) {
            value = navigationParameterStore.get(key);
            navigationParameterStore.remove(key);
        }
        return value != null ? (T)value : null;
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        Object value = null;
        if (navigationParameterStore.containsKey(key)) {
            value = navigationParameterStore.get(key);
        }
        return value != null ? (T)value : null;
    }

    @Override
    public void put(String key, Object value) {
        navigationParameterStore.put(key, value);
    }

    @Override
    public void remove(String key) {
        if(navigationParameterStore.containsKey(key))
            navigationParameterStore.remove(key);
    }
}
