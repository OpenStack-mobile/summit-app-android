package org.openstack.android.summit.common;

/**
 * Created by Claudio Redi on 1/19/2016.
 */
public interface INavigationParametersStore {

    <T> T pop(String key, Class<T> type);

    <T> T get(String key, Class<T> type);

    void put(String key, Object value);

    void remove(String key);
}
