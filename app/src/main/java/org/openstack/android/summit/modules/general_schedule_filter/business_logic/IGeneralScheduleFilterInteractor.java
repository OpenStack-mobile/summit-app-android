package org.openstack.android.summit.modules.general_schedule_filter.business_logic;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.DTOs.TrackDTO;
import org.openstack.android.summit.common.DTOs.TrackGroupDTO;
import org.openstack.android.summit.common.DTOs.VenueFilterDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;

import java.util.List;

/**
 * Created by Claudio Redi on 2/2/2016.
 */
public interface IGeneralScheduleFilterInteractor extends IBaseInteractor {

    List<NamedDTO> getSummitTypes();

    List<NamedDTO> getEventTypes();

    List<String> getLevels();

    List<TrackGroupDTO> getTrackGroups();

    List<TrackDTO> getTracksForGroup(int trackGroupId);

    TrackGroupDTO getTrackGroup(int trackGroupId);

    List<String> getTags();

    SummitDTO getActiveSummit();

    List<VenueFilterDTO> getVenues();

    boolean groupIncludesAnyOfGivenTracks(int trackGroupId,  List<Integer> tracksIds);

    boolean VenueIncludesAnyOfGivenRooms(int venueId, List<Integer> roomIds);

    List<TrackDTO>  getTracksBelongingToGroup(int trackGroupId,  List<Integer> tracksIds);

    List<NamedDTO>  getRoomsBelongingToVenue(int venueId,  List<Integer> roomsIds);

    NamedDTO getVenue(int venueId);

    List<NamedDTO> getRoomsForVenue(int venueId);

}
