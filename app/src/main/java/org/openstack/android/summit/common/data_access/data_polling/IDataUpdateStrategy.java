package org.openstack.android.summit.common.data_access.data_polling;

import org.openstack.android.summit.common.entities.DataUpdate;

/**
 * Created by Claudio Redi on 2/8/2016.
 */
public interface IDataUpdateStrategy {
    void process(DataUpdate dataUpdate) throws DataUpdateException;
}
