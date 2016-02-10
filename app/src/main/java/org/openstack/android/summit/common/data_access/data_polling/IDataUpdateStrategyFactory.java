package org.openstack.android.summit.common.data_access.data_polling;

/**
 * Created by Claudio Redi on 2/8/2016.
 */
public interface IDataUpdateStrategyFactory {
    IDataUpdateStrategy create(String className);
}
