package org.openstack.android.summit.common.data_access.repositories;

import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;

import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public interface ISummitAttendeeDataStore extends IGenericDataStore<SummitAttendee> {

    Observable<Boolean> addEventToMemberSchedule(SummitAttendee me, SummitEvent summitEvent);

    void addEventToMemberScheduleLocal(SummitAttendee me, SummitEvent summitEvent);

    Observable<Boolean> removeEventFromMemberSchedule(SummitAttendee me, SummitEvent summitEvent);

    Observable<Boolean> deleteRSVP(SummitAttendee me, SummitEvent summitEvent);

    void removeEventFromMemberScheduleLocal(SummitAttendee me, SummitEvent summitEvent);

}
