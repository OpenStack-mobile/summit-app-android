package org.openstack.android.summit.modules.search.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.business_logic.IScheduleableInteractor;
import org.openstack.android.summit.common.data_access.repositories.IPresentationSpeakerDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITrackDataStore;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public class SearchInteractor extends BaseInteractor implements ISearchInteractor, IScheduleableInteractor {

    IScheduleableInteractor scheduleableInteractor;
    ISummitEventDataStore summitEventDataStore;
    ITrackDataStore trackDataStore;
    IPresentationSpeakerDataStore presentationSpeakerDataStore;

    public SearchInteractor
    (
        ISecurityManager securityManager,
        IScheduleableInteractor scheduleableInteractor,
        ISummitEventDataStore summitEventDataStore,
        ITrackDataStore trackDataStore,
        IPresentationSpeakerDataStore presentationSpeakerDataStore,
        IDTOAssembler dtoAssembler,
        ISummitDataStore summitDataStore,
        ISummitSelector summitSelector
    )
    {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore);
        this.scheduleableInteractor       = scheduleableInteractor;
        this.summitEventDataStore         = summitEventDataStore;
        this.trackDataStore               = trackDataStore;
        this.presentationSpeakerDataStore = presentationSpeakerDataStore;
    }

    @Override
    public Observable<Boolean> addEventToLoggedInMemberSchedule(int eventId) {
        return scheduleableInteractor.addEventToLoggedInMemberSchedule(eventId);
    }

    @Override
    public Observable<Boolean> removeEventFromLoggedInMemberSchedule(int eventId)
    {
        return scheduleableInteractor.removeEventFromLoggedInMemberSchedule(eventId);
    }

    @Override
    public boolean isEventScheduledByLoggedMember(int eventId) {
        return scheduleableInteractor.isEventScheduledByLoggedMember(eventId);
    }

    @Override
    public boolean isEventFavoriteByLoggedMember(int eventId) {
        return scheduleableInteractor.isEventFavoriteByLoggedMember(eventId);
    }

    @Override
    public Observable<Boolean> addEventToMyFavorites(int eventId) {
        return scheduleableInteractor.addEventToMyFavorites(eventId);
    }

    @Override
    public Observable<Boolean> removeEventFromMemberFavorites(int eventId) {
        return scheduleableInteractor.removeEventFromMemberFavorites(eventId);
    }

    @Override
    public boolean shouldShowVenues() {
        return scheduleableInteractor.shouldShowVenues();
    }

    @Override
    public List<ScheduleItemDTO> getEventsBySearchTerm(String searchTerm) {
        List<SummitEvent> events = summitEventDataStore.getBySearchTerm
        (
            summitSelector.getCurrentSummitId(),
            searchTerm
        );
        return createDTOList(events, ScheduleItemDTO.class);
    }

    @Override
    public List<NamedDTO> getTracksBySearchTerm(String searchTerm) {
        List<Track> tracks                        = trackDataStore.getAllOrderedByName(summitSelector.getCurrentSummitId());
        ArrayList<Track> tracksMatchingSearchTerm = new ArrayList<>();

        for(Track track: tracks) {
            if (track.getName().toLowerCase().contains(searchTerm.toLowerCase()) && summitEventDataStore.countByTrack(track.getId()) > 0 ) {
                tracksMatchingSearchTerm.add(track);
            }
        }
        return createDTOList(tracksMatchingSearchTerm, NamedDTO.class);
    }

    @Override
    public List<PersonListItemDTO> getSpeakersBySearchTerm(String searchTerm) {
        List<PresentationSpeaker> speakers = presentationSpeakerDataStore.getByFilter
        (
            summitSelector.getCurrentSummitId(),
            searchTerm,
            1,
            Integer.MAX_VALUE
        );
        return createDTOList(speakers, PersonListItemDTO.class);
    }
}
