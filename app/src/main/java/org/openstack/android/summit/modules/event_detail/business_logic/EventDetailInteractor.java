package org.openstack.android.summit.modules.event_detail.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.EventDetailDTO;
import org.openstack.android.summit.common.DTOs.FeedbackDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.ScheduleableInteractor;
import org.openstack.android.summit.common.data_access.repositories.IMemberDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 1/21/2016.
 */
public class EventDetailInteractor extends ScheduleableInteractor implements IEventDetailInteractor {
    IReachability reachability;

    @Inject
    public EventDetailInteractor
    (
        ISummitEventDataStore summitEventDataStore,
        ISummitDataStore summitDataStore,
        IMemberDataStore memberDataStore,
        IReachability reachability,
        IDTOAssembler dtoAssembler,
        ISecurityManager securityManager,
        IPushNotificationsManager pushNotificationsManager,
        ISummitSelector summitSelector
    )
    {
        super(summitEventDataStore, summitDataStore, memberDataStore, dtoAssembler, securityManager, pushNotificationsManager, summitSelector);
        this.reachability = reachability;
    }

    @Override
    public EventDetailDTO getEventDetail(int eventId) {
        SummitEvent summitEvent  = summitEventDataStore.getById(eventId);
        EventDetailDTO dto =  summitEvent != null ? dtoAssembler.createDTO(summitEvent, EventDetailDTO.class):null;
        if(this.isMemberLoggedIn()){
            dto.setScheduled(this.isEventScheduledByLoggedMember(dto.getId()));
            dto.setFavorite(this.isEventFavoriteByLoggedMember(dto.getId()));
        }
        return dto;
    }

    public FeedbackDTO getMyFeedbackForEvent(int eventId) {

        if (securityManager.isLoggedIn()) {
            Feedback feedback = securityManager.getCurrentMember().getFeedback().where().equalTo("event.id", eventId).findFirst();
            if (feedback == null) return null;
            return dtoAssembler.createDTO(feedback, FeedbackDTO.class);
        }
        return null;
    }

    @Override
    public Observable<List<FeedbackDTO>> getFeedbackForEvent(int eventId, int page, int objectsPerPage) {
        return summitEventDataStore
                .getFeedback(eventId, page, objectsPerPage)
                .map( data -> createDTOList(data, FeedbackDTO.class));
    }

    @Override
    public Observable<Double> getAverageFeedbackForEvent(int eventId) {
        return summitEventDataStore.getAverageFeedback(eventId);
    }
}
