package org.openstack.android.summit.common.business_logic;

import org.openstack.android.summit.common.IBaseWireframe;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public interface IScheduleableInteractor extends IBaseInteractor  {
    void addEventToLoggedInMemberSchedule(int eventId, IInteractorAsyncOperationListener<Void> interactorAsyncOperationListener);

    void removeEventFromLoggedInMemberSchedule(int eventId, IInteractorAsyncOperationListener<Void> interactorAsyncOperationListener);

    Boolean isEventScheduledByLoggedMember(int eventId);

    Boolean isMemberLoggedIn();

    boolean shouldShowVenues();
}
