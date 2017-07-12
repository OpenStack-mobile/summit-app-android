package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.repositories.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.SummitAttendee;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public class SummitAttendeeDataStore
        extends GenericDataStore<SummitAttendee>
        implements ISummitAttendeeDataStore {


    public SummitAttendeeDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(SummitAttendee.class, saveOrUpdateStrategy, deleteStrategy);
    }

}
