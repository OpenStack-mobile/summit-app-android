package org.openstack.android.summit.common.business_logic;

import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public interface IScheduleableInteractor extends IBaseInteractor  {

    Observable<Boolean> addEventToLoggedInMemberSchedule(int eventId);

    Observable<Boolean> removeEventFromLoggedInMemberSchedule(int eventId);

    boolean isEventScheduledByLoggedMember(int eventId);

    boolean isEventFavoriteByLoggedMember(int eventId);

    Observable<Boolean> addEventToMyFavorites(int eventId);

    Observable<Boolean> removeEventFromMemberFavorites(int eventId);

    boolean shouldShowVenues();

    Observable<Boolean> deleteRSVP(int eventId);
}
