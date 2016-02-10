package org.openstack.android.summit.modules.track_list.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.TrackDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public class TrackListInteractor extends BaseInteractor implements ITrackListInteractor {
    private IDTOAssembler dtoAssembler;
    private IGenericDataStore genericDataStore;

    @Inject
    public TrackListInteractor(IDTOAssembler dtoAssembler, IGenericDataStore genericDataStore) {
        super(dtoAssembler);
        this.genericDataStore = genericDataStore;
    }

    @Override
    public List<TrackDTO> getTracks(List<Integer> trackGroups) {
        List<Track> tracks = genericDataStore.getaAllLocal(Track.class);
        ArrayList<Track> filteredTracks = new ArrayList<>();

        if (trackGroups != null && trackGroups.size() > 0) {
            for(Track track: tracks) {
                if (trackMatchFilter(track, trackGroups)) {
                    filteredTracks.add(track);
                }
            }
            tracks = filteredTracks;
        }

        Comparator<Track> comparator = new Comparator<Track>() {
            @Override
            public int compare(Track c1, Track c2) {
                return c1.getName().compareTo(c2.getName());
            }
        };

        Collections.sort(tracks, comparator);

        List<TrackDTO> dtos = createDTOList(tracks, TrackDTO.class);

        return dtos;
    }

    private boolean trackMatchFilter(Track track, List<Integer> trackGroupIds) {
        boolean isMatch = false;
        if (track.getTrackGroups().size() > 0) {
            for(TrackGroup trackGroup: track.getTrackGroups()) {
                if (trackGroupIds.contains(trackGroup.getId())) {
                    return true;
                }
            }
        }
        return isMatch;
    }
}
