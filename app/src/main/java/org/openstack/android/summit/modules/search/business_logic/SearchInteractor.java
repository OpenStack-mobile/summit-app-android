package org.openstack.android.summit.modules.search.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.business_logic.IScheduleableInteractor;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.IPresentationSpeakerDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public class SearchInteractor extends BaseInteractor implements ISearchInteractor, IScheduleableInteractor {

    IScheduleableInteractor scheduleableInteractor;
    ISummitEventDataStore summitEventDataStore;
    IGenericDataStore genericDataStore;
    IPresentationSpeakerDataStore presentationSpeakerDataStore;

    public SearchInteractor(IScheduleableInteractor scheduleableInteractor, ISummitEventDataStore summitEventDataStore, IGenericDataStore genericDataStore, IPresentationSpeakerDataStore presentationSpeakerDataStore, IDTOAssembler dtoAssembler) {
        super(dtoAssembler);
        this.scheduleableInteractor = scheduleableInteractor;
        this.summitEventDataStore = summitEventDataStore;
        this.genericDataStore = genericDataStore;
        this.presentationSpeakerDataStore = presentationSpeakerDataStore;
    }

    @Override
    public void addEventToLoggedInMemberSchedule(int eventId, IInteractorAsyncOperationListener<Void> interactorAsyncOperationListener) {
        scheduleableInteractor.addEventToLoggedInMemberSchedule(eventId, interactorAsyncOperationListener);
    }

    @Override
    public void removeEventFromLoggedInMemberSchedule(int eventId, IInteractorAsyncOperationListener<Void> interactorAsyncOperationListener) {
        scheduleableInteractor.removeEventFromLoggedInMemberSchedule(eventId, interactorAsyncOperationListener);
    }

    @Override
    public Boolean isEventScheduledByLoggedMember(int eventId) {
        return scheduleableInteractor.isEventScheduledByLoggedMember(eventId);
    }

    @Override
    public Boolean isMemberLoggedInConfirmedAttendee() {
        return scheduleableInteractor.isMemberLoggedInConfirmedAttendee();
    }

    @Override
    public boolean shouldShowVenues() {
        return scheduleableInteractor.shouldShowVenues();
    }

    @Override
    public List<ScheduleItemDTO> getEventsBySearchTerm(String searchTerm) {
        List<SummitEvent> events = summitEventDataStore.getBySearchTerm(searchTerm);
        List<ScheduleItemDTO> dtos = createDTOList(events, ScheduleItemDTO.class);
        return dtos;
    }

    @Override
    public List<NamedDTO> getTracksBySearchTerm(String searchTerm) {
        List<Track> tracks = genericDataStore.getAllLocal(Track.class);
        ArrayList<Track> tracksMatchingSearchTerm = new ArrayList<>();

        for(Track track: tracks) {
            if (track.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                tracksMatchingSearchTerm.add(track);
            }
        }
        List<NamedDTO> dtos = createDTOList(tracksMatchingSearchTerm, NamedDTO.class);
        return dtos;
    }

    @Override
    public List<PersonListItemDTO> getSpeakersBySearchTerm(String searchTerm) {
        List<PresentationSpeaker> speakers = presentationSpeakerDataStore.getByFilterLocal(searchTerm, 1, 10000);

        List<PersonListItemDTO> dtos = createDTOList(speakers, PersonListItemDTO.class);
        return dtos;
    }
}
