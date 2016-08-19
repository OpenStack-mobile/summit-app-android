package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public interface ISummitAttendeeRemoteDataStore {

    void addEventToSchedule(SummitAttendee summitAttendee, SummitEvent summitEvent, IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener);

    void removeEventFromSchedule(SummitAttendee summitAttendee, SummitEvent summitEvent, IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener);

}
