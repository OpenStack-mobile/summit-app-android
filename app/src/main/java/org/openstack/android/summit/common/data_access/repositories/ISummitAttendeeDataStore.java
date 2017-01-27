package org.openstack.android.summit.common.data_access.repositories;

import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public interface ISummitAttendeeDataStore extends IGenericDataStore<SummitAttendee> {

    void addEventToMemberSchedule(SummitAttendee summitAttendee, SummitEvent summitEvent, IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener);

    void addEventToMemberScheduleLocal(SummitAttendee summitAttendee, SummitEvent summitEvent);

    void removeEventFromMemberSchedule(SummitAttendee summitAttendee, SummitEvent summitEvent, IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener);

    void removeEventFromMemberScheduleLocal(SummitAttendee summitAttendee, SummitEvent summitEvent);

}
