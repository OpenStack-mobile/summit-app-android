package org.openstack.android.summit.modules.general_schedule_filter.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.TrackDTO;
import org.openstack.android.summit.common.DTOs.TrackGroupDTO;
import org.openstack.android.summit.common.DTOs.VenueFilterDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.IEventTypeDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitTypeDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITagDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITrackGroupDataStore;
import org.openstack.android.summit.common.data_access.repositories.IVenueDataStore;
import org.openstack.android.summit.common.entities.EventType;
import org.openstack.android.summit.common.entities.SummitType;
import org.openstack.android.summit.common.entities.Tag;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.entities.VenueFloor;
import org.openstack.android.summit.common.entities.VenueRoom;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import java.util.ArrayList;
import java.util.List;
import io.realm.Sort;

/**
 * Created by Claudio Redi on 2/1/2016.
 */
public class GeneralScheduleFilterInteractor
        extends BaseInteractor
        implements IGeneralScheduleFilterInteractor {

    private ISummitTypeDataStore summitTypeDataStore;
    private ITagDataStore        tagDataStore;
    private IEventTypeDataStore  eventTypeDataStore;
    private ISummitEventDataStore summitEventDataStore;
    private IVenueDataStore venueDataStore;
    private ITrackGroupDataStore trackGroupDataStore;

    public GeneralScheduleFilterInteractor
    (
        ISecurityManager securityManager,
        ISummitDataStore summitDataStore,
        ISummitEventDataStore summitEventDataStore,
        IVenueDataStore venueDataStore,
        ITrackGroupDataStore trackGroupDataStore,
        ISummitTypeDataStore summitTypeDataStore,
        IEventTypeDataStore eventTypeDataStore,
        ITagDataStore tagDataStore,
        IDTOAssembler dtoAssembler,
        ISummitSelector summitSelector,
        IReachability reachability
    )
    {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore, reachability);

        this.venueDataStore       = venueDataStore;
        this.summitEventDataStore = summitEventDataStore;
        this.summitTypeDataStore  = summitTypeDataStore;
        this.eventTypeDataStore   = eventTypeDataStore;
        this.tagDataStore         = tagDataStore;
        this.trackGroupDataStore  = trackGroupDataStore;
    }

    @Override
    public List<NamedDTO> getSummitTypes() {
        List<SummitType> summitTypes = summitTypeDataStore.getAll(new String[] { "name"}, new Sort[]{ Sort.ASCENDING });
        List<SummitType> results     =  new ArrayList<>();
        for(SummitType summitType: summitTypes){
            if ( summitEventDataStore.countBySummitType(summitType.getId()) > 0)
                results.add(summitType);
        }
        return createDTOList(results, NamedDTO.class);
    }

    @Override
    public List<NamedDTO> getEventTypes() {
        List<EventType> eventTypes = eventTypeDataStore.getAll(new String[] { "name"}, new Sort[]{ Sort.ASCENDING });
        List<EventType> results    = new ArrayList<>();
        for(EventType eventType: eventTypes){
            if ( summitEventDataStore.countByEventType(eventType.getId()) > 0)
                results.add(eventType);
        }
        return createDTOList(results, NamedDTO.class);
    }

    @Override
    public List<String> getLevels() {
        return summitEventDataStore.getPresentationLevels();
    }

    @Override
    public List<VenueFilterDTO> getVenues() {
        List<Venue> venues = venueDataStore.getAllBySummit(summitSelector.getCurrentSummitId());
        return createDTOList(venues, VenueFilterDTO.class);
    }

    @Override
    public boolean groupIncludesAnyOfGivenTracks(int trackGroupId, List<Integer> tracksIds) {
        if(tracksIds.isEmpty()) return false;
        TrackGroup trackGroup = trackGroupDataStore.getById(trackGroupId);
        if(trackGroup == null) return false;
        List<Integer> tracksOnGroup = new ArrayList<>();
        for(Track track: trackGroup.getTracks())
            tracksOnGroup.add(track.getId());

        for(Integer testId:tracksIds){
            if(tracksOnGroup.contains(testId)) return true;
        }
        return false;
    }

    @Override
    public boolean VenueIncludesAnyOfGivenRooms(int venueId, List<Integer> roomsIds) {
        if(roomsIds.isEmpty()) return false;
        Venue venue = venueDataStore.getById(venueId);
        if(venue == null) return false;

        // rooms without floor
        for(VenueRoom room:venue.getRooms().sort("name")){
            if(roomsIds.contains(room.getId()))
                return true;
        }

        List<VenueFloor> floors   = venue.getFloors();
        for(VenueFloor floor: floors){
            for(VenueRoom  room: floor.getRooms()){
                if(roomsIds.contains(room.getId()))
                  return true;
            }
        }
        return false;
    }

    @Override
    public List<TrackDTO> getTracksBelongingToGroup(int trackGroupId, List<Integer> tracksIds) {
        List<TrackDTO> result = new ArrayList<>();
        if(tracksIds.isEmpty()) return result;
        TrackGroup trackGroup = trackGroupDataStore.getById(trackGroupId);
        if(trackGroup == null) return result;
        for(Track track: trackGroup.getTracks())
            if(tracksIds.contains(track.getId()))
                result.add(createDTO(track, TrackDTO.class));
        return result;
    }

    @Override
    public List<NamedDTO> getRoomsBelongingToVenue(int venueId, List<Integer> roomsIds) {
        List<NamedDTO> result = new ArrayList<>();
        if(roomsIds.isEmpty()) return result;
        Venue venue = venueDataStore.getById(venueId);
        if(venue == null) return result;

        // rooms without floor
        for(VenueRoom room:venue.getRooms().sort("name")){
            if(!roomsIds.contains(room.getId())) continue;
            result.add(createDTO(room, NamedDTO.class));
        }

        List<VenueFloor> floors   = venue.getFloors().sort("number");
        for(VenueFloor floor: floors){
            for(VenueRoom  room: floor.getRooms()){
                if(!roomsIds.contains(room.getId())) continue;
                result.add(createDTO(room, NamedDTO.class));
            }
        }

        return result;
    }

    @Override
    public NamedDTO getVenue(int venueId) {
        Venue venue = venueDataStore.getById(venueId);
        if(venue == null) return null;
        return createDTO(venue, NamedDTO.class);
    }

    @Override
    public List<NamedDTO> getRoomsForVenue(int venueId) {
        Venue venue = venueDataStore.getById(venueId);
        if(venue == null) return new ArrayList<>();
        List<NamedDTO> rooms = new ArrayList<>();
        // rooms without floor
        for(VenueRoom room:venue.getRooms().sort("name")){
            if(!summitEventDataStore.existEventsOnRoom(room.getId())) continue;
            rooms.add(createDTO(room, NamedDTO.class));
        }
        // rooms with floors
        List<VenueFloor> floors   = venue.getFloors().sort("number");
        for(VenueFloor floor: floors){
            List<VenueRoom> results = new ArrayList<>();
            for (VenueRoom roomOnFloor:floor.getRooms()) {
                if (!summitEventDataStore.existEventsOnRoom(roomOnFloor.getId())) continue;
                results.add(roomOnFloor);
            }

            List<NamedDTO> dtos     = createDTOList(results, NamedDTO.class);
            rooms.addAll(dtos);
        }
        return rooms;
    }

    @Override
    public List<TrackGroupDTO> getTrackGroups() {
        List<TrackGroup> trackGroups = trackGroupDataStore.getAllBySummit(summitSelector.getCurrentSummitId());
        List<TrackGroup> results = new ArrayList<>();
        for(TrackGroup trackGroup: trackGroups){
            if ( summitEventDataStore.countByTrackGroup(trackGroup.getId()) > 0)
                results.add(trackGroup);
        }
        return createDTOList(results, TrackGroupDTO.class);
    }

    @Override
    public List<TrackDTO> getTracksForGroup(int trackGroupId) {
        TrackGroup  group     = trackGroupDataStore.getById(trackGroupId);
        if( group == null) return new ArrayList<>();
        List<Track> results   = trackGroupDataStore.getTracks(trackGroupId);
        List<TrackDTO> tracks = createDTOList(results, TrackDTO.class);
        TrackGroupDTO trackGroupDTO = createDTO(group, TrackGroupDTO.class);
        for (TrackDTO track: tracks){
            track.setTrackGroup(trackGroupDTO);
        }
        return tracks;
    }

    @Override
    public TrackGroupDTO getTrackGroup(int trackGroupId) {
        TrackGroup trackGroup = trackGroupDataStore.getById(trackGroupId);
        if(trackGroup == null) return null;
        return createDTO(trackGroup, TrackGroupDTO.class);
    }

    @Override
    public List<String> getTags() {
        List<Tag> tags = tagDataStore.getAll();
        List<String> dtos = new ArrayList<>();
        for (Tag tag: tags) {
            dtos.add(tag.getTag());
        }
        return dtos;
    }

}