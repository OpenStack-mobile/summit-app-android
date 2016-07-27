package org.openstack.android.summit.modules.event_detail.business_logic;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.EventDetailDTO;
import org.openstack.android.summit.common.DTOs.FeedbackDTO;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.business_logic.ScheduleableInteractor;
import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/21/2016.
 */
public class EventDetailInteractor extends ScheduleableInteractor implements IEventDetailInteractor {
    IReachability reachability;

    @Inject
    public EventDetailInteractor(ISummitEventDataStore summitEventDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, ISummitDataStore summitDataStore, IReachability reachability, IDTOAssembler dtoAssembler, ISecurityManager securityManager, IDataUpdatePoller dataUpdatePoller, IPushNotificationsManager pushNotificationsManager) {
        super(summitEventDataStore, summitAttendeeDataStore, summitDataStore, dtoAssembler, securityManager, dataUpdatePoller, pushNotificationsManager);
        this.reachability = reachability;
    }

    @Override
    public EventDetailDTO getEventDetail(int eventId) {
        SummitEvent summitEvent = summitEventDataStore.getByIdLocal(eventId);
        EventDetailDTO dto = dtoAssembler.createDTO(summitEvent, EventDetailDTO.class);
        return dto;
    }

    public FeedbackDTO getMyFeedbackForEvent(int eventId) {

        FeedbackDTO dto = null;
        Member currentMember = securityManager.getCurrentMember();

        if (securityManager.isLoggedInAndConfirmedAttendee() && currentMember != null) {

            Feedback feedback = currentMember.getAttendeeRole().getFeedback().where().equalTo("event.id", eventId).findFirst();
            if (feedback != null) {
                dto = dtoAssembler.createDTO(feedback, FeedbackDTO.class);
            }
        }
        return dto;
    }

    @Override
    public void getFeedbackForEvent(int eventId, int page, int objectsPerPage, final IInteractorAsyncOperationListener<List<FeedbackDTO>> interactorAsyncOperationListener) {
        if (!reachability.isNetworkingAvailable(OpenStackSummitApplication.context)) {
            String error = "There is no network connectivity. Unable to load event feedback.";
            interactorAsyncOperationListener.onError(error);
            return;
        }

        IDataStoreOperationListener<Feedback> dataStoreOperationListener = new DataStoreOperationListener<Feedback>() {
            @Override
            public void onSucceedWithDataCollection(List<Feedback> data) {
                super.onSucceedWithDataCollection(data);
                List<FeedbackDTO> dtos = createDTOList(data, FeedbackDTO.class);
                interactorAsyncOperationListener.onSucceedWithData(dtos);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                interactorAsyncOperationListener.onError(message);
            }
        };

        summitEventDataStore.getFeedbackOrigin(eventId, page, objectsPerPage, dataStoreOperationListener);
    }

    public void getAverageFeedbackForEvent(int eventId, final IInteractorAsyncOperationListener<Double> interactorAsyncOperationListener) {
        IDataStoreOperationListener<SummitEvent> dataStoreOperationListener = new DataStoreOperationListener<SummitEvent>() {
            @Override
            public void onSucceedWithSingleData(SummitEvent data) {
                super.onSucceedWithSingleData(data);
                interactorAsyncOperationListener.onSucceedWithData(data.getAverageRate());
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                interactorAsyncOperationListener.onError(message);
            }
        };

        summitEventDataStore.getAverageFeedbackOrigin(eventId, dataStoreOperationListener);
    }
}
