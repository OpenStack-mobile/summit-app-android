package org.openstack.android.summit.common.business_logic;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.Date;
import java.util.List;

/**
 * Created by Claudio Redi on 1/4/2016.
 */
public class ScheduleableInteractor extends BaseInteractor implements IScheduleableInteractor {
    protected ISecurityManager securityManager;
    protected ISummitEventDataStore summitEventDataStore;
    protected ISummitAttendeeDataStore summitAttendeeDataStore;
    protected ISummitDataStore summitDataStore;

    public ScheduleableInteractor(ISummitEventDataStore summitEventDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, ISummitDataStore summitDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager, IDataUpdatePoller dataUpdatePoller) {
        super(dtoAssembler, dataUpdatePoller);
        this.securityManager = securityManager;
        this.summitEventDataStore = summitEventDataStore;
        this.summitAttendeeDataStore = summitAttendeeDataStore;
        this.summitDataStore = summitDataStore;
    }

    @Override
    public void addEventToLoggedInMemberSchedule(int eventId, final IInteractorAsyncOperationListener<Void> interactorAsyncOperationListener) {
        Member loggedInMember = securityManager.getCurrentMember();
        SummitEvent summitEvent = summitEventDataStore.getByIdLocal(eventId);
        IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener = new DataStoreOperationListener<SummitAttendee>() {
            @Override
            public void onSucceedWithoutData() {
                interactorAsyncOperationListener.onSucceed();
            }

            @Override
            public void onError(String message) {
                interactorAsyncOperationListener.onError(message);
            }
        };

        summitAttendeeDataStore.addEventToMemberShedule(loggedInMember.getAttendeeRole(), summitEvent, dataStoreOperationListener);
    }

    @Override
    public void removeEventFromLoggedInMemberSchedule(int eventId, final IInteractorAsyncOperationListener<Void> interactorAsyncOperationListener) {
        Member loggedInMember = securityManager.getCurrentMember();
        if (loggedInMember == null) {
            interactorAsyncOperationListener.onError(OpenStackSummitApplication.context.getResources().getString(R.string.no_logged_in_user));
            return;
        }
        SummitEvent summitEvent = summitEventDataStore.getByIdLocal(eventId);
        IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener = new DataStoreOperationListener<SummitAttendee>() {
            @Override
            public void onSucceedWithoutData() {
                interactorAsyncOperationListener.onSucceed();
            }

            @Override
            public void onError(String message) {
                interactorAsyncOperationListener.onError(message);
            }
        };

        summitAttendeeDataStore.removeEventFromMemberShedule(loggedInMember.getAttendeeRole(), summitEvent, dataStoreOperationListener);
    }

    @Override
    public Boolean isEventScheduledByLoggedMember(int eventId) {
        Member loggedInMember = securityManager.getCurrentMember();

        if (loggedInMember == null) {
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
    public Boolean isMemberLoggedIn() {
        return securityManager.isLoggedIn();
    }

    @Override
    public boolean shouldShowVenues() {
        Summit summit = summitDataStore.getActiveLocal();
        return summit.getStartShowingVenuesDate().getTime() < new Date().getTime();
    }
}