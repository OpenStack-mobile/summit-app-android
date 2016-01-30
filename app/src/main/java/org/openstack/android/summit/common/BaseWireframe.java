package org.openstack.android.summit.common;

/**
 * Created by Claudio Redi on 1/19/2016.
 */
public class BaseWireframe implements IBaseWireframe {
    protected INavigationParametersStore navigationParametersStore;

    public BaseWireframe(INavigationParametersStore navigationParametersStore) {
        this.navigationParametersStore = navigationParametersStore;
    }

    @Override
    public <T> T getParameter(String key, Class<T> type) {
        return navigationParametersStore.get(key, type);
    }
}
