package org.openstack.android.summit.common;

/**
 * Created by Claudio Redi on 1/19/2016.
 */
public interface IBaseWireframe {
    <T> T getParameter(String key, Class<T> type);
}
