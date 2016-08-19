package org.openstack.android.summit.common.business_logic;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.Date;

/**
 * Created by Claudio Redi on 1/4/2016.
 */
public class ScheduleableInteractor extends BaseInteractor implements IScheduleableInteractor {

    protected ISecurityManager          securityManager;
    protected ISummitEventDataStore     summitEventDataStore;
    protected ISummitAttendeeDataStore  summitAttendeeDataStore;
    protected ISummitDataStore          summitDataStore;
    protected IPushNotificationsManager pushNotificationsManager;

    public ScheduleableInteractor(ISummitEventDataStore summitEventDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, ISummitDataStore summitDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager, IPushNotificationsManager pushNotificationsManager) {
        super(dtoAssembler);

        this.securityManager          = securityManager;
        this.summitEventDataStore     = summitEventDataStore;
        this.summitAttendeeDataStore  = summitAttendeeDataStore;
        this.summitDataStore          = summitDataStore;
        this.pushNotificationsManager = pushNotificationsManager;
    }

    @Override
    public void addEventToLoggedInMemberSchedule(int eventId, final IInteractorAsyncOperationListener<Void> interactorAsyncOperationListener) {
        if (!securityManager.isLoggedInAndConfirmedAttendee()) {
            interactorAsyncOperationListener.onError(OpenStackSummitApplication.context.getResources().getString(R.string.no_logged_in_user));
            return;
        }

        final Member loggedInMember   = securityManager.getCurrentMember();
        final SummitEvent summitEvent = summitEventDataStore.getByIdLocal(eventId);

        IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener = new DataStoreOperationListener<SummitAttendee>() {
            @Override
            public void onSucceedWithoutData() {
                //remove channel
                pushNotificationsManager.subscribeMember(loggedInMember, summitEvent.getSummit());
                interactorAsyncOperationListener.onSucceed();
            }

            @Override
            public void onError(String message) {
                interactorAsyncOperationListener.onError(message);
            }
        };

        summitAttendeeDataStore.addEventToMemberSchedule(loggedInMember.getAttendeeRole(), summitEvent, dataStoreOperationListener);
    }

    @Override
    public void removeEventFromLoggedInMemberSchedule(int eventId, final IInteractorAsyncOperationListener<Void> interactorAsyncOperationListener) {
        if (!securityManager.isLoggedInAndConfirmedAttendee()) {
            interactorAsyncOperationListener.onError(OpenStackSummitApplication.context.getResources().getString(R.string.no_logged_in_user));
            return;
        }

        final Member loggedInMember   = securityManager.getCurrentMember();
        final SummitEvent summitEvent = summitEventDataStore.getByIdLocal(eventId);

        IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener = new DataStoreOperationListener<SummitAttendee>() {
            @Override
            public void onSucceedWithoutData() {
                // remove channel
                pushNotificationsManager.subscribeMember(loggedInMember, summitEvent.getSummit());
                interactorAsyncOperationListener.onSucceed();
            }

            @Override
            public void onError(String message) {
                interactorAsyncOperationListener.onError(message);
            }
        };

        summitAttendeeDataStore.removeEventFromMemberSchedule(loggedInMember.getAttendeeRole(), summitEvent, dataStoreOperationListener);
    }

    @Override
    public boolean isEventScheduledByLoggedMember(int eventId) {

        if (!securityManager.isLoggedInAndConfirmedAttendee()) {
            return false;
        }

        Member loggedInMember = securityManager.getCurrentMember();

        if (loggedInMember == null || loggedInMember.getAttendeeRole() == null) {
            return false;
        }

        Boolean found = false;
        for (SummitEvent event : loggedInMember.getAttendeeRole().getScheduledEvents()) {
            if (event.getId() == eventId) {
                found = true;
                break;
            }
        }

        return found;
    }

    @Override
    public boolean isMemberLogged() {
        return securityManager.isLoggedIn();
    }

    @Override
    public boolean isMemberLoggedAndConfirmedAttendee() {
        return securityManager.isLoggedInAndConfirmedAttendee();
    }

    @Override
    public boolean shouldShowVenues() {
        Summit summit = summitDataStore.getActiveLocal();
        return summit.getStartShowingVenuesDate().getTime() < new Date().getTime();
    }
}