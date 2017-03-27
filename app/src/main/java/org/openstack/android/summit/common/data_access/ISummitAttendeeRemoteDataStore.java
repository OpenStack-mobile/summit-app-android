package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;

import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public interface ISummitAttendeeRemoteDataStore {

    Observable<Boolean> addEventToSchedule(SummitAttendee summitAttendee, SummitEvent summitEvent);

    Observable<Boolean> removeEventFromSchedule(SummitAttendee summitAttendee, SummitEvent summitEvent);

    Observable<Boolean> deleteRSVP(SummitAttendee summitAttendee, SummitEvent summitEvent);

}
