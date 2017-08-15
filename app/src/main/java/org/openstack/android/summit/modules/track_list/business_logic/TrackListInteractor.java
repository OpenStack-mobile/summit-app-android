package org.openstack.android.summit.modules.track_list.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.TrackDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITrackDataStore;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public class TrackListInteractor extends BaseInteractor implements ITrackListInteractor {

    private ISummitEventDataStore summitEventDataStore;
    private ITrackDataStore trackDataStore;

    @Inject
    public TrackListInteractor
    (
            ISecurityManager securityManager,
            IDTOAssembler dtoAssembler,
            ISummitEventDataStore summitEventDataStore,
            ITrackDataStore trackDataStore,
            ISummitDataStore summitDataStore,
            ISummitSelector summitSelector,
            IReachability reachability
    ) {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore, reachability);
        this.summitEventDataStore = summitEventDataStore;
        this.trackDataStore       = trackDataStore;
    }

    @Override
    public List<TrackDTO> getTracks(List<Integer> trackGroups) {
        List<Track> tracks              = trackDataStore.getAllOrderedByName(summitSelector.getCurrentSummitId());
        ArrayList<Track> filteredTracks = new ArrayList<>();

        for(Track track: tracks) {
            if (summitEventDataStore.countByTrack(track.getId()) > 0) {
                if(trackGroups.size() > 0 && !trackMatchFilter(track, trackGroups)) continue;
                filteredTracks.add(track);
            }
        }

        return createDTOList(filteredTracks, TrackDTO.class);
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
