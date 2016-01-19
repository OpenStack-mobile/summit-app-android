package org.openstack.android.summit.common;

/**
 * Created by Claudio Redi on 1/19/2016.
 */
public interface INavigationParametersStore {
    <T> T get(String key, Class<T> type);

    void put(String key, Object value);
}
