package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public interface ISummitAttendeeDataStore {
    void addEventToMemberShedule(SummitAttendee summitAttendee, SummitEvent summitEvent, IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener);

    void addEventToMemberSheduleLocal(SummitAttendee summitAttendee, SummitEvent summitEvent);

    void removeEventFromMemberShedule(SummitAttendee summitAttendee, SummitEvent summitEvent, IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener);

    void removeEventFromMemberSheduleLocal(SummitAttendee summitAttendee, SummitEvent summitEvent);

    void addFeedback(SummitAttendee attendee, Feedback feedback, IDataStoreOperationListener dataStoreOperationListener);
}
