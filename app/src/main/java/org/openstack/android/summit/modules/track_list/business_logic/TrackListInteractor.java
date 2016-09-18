package org.openstack.android.summit.modules.track_list.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.TrackDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import io.realm.Sort;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public class TrackListInteractor extends BaseInteractor implements ITrackListInteractor {

    private IDTOAssembler dtoAssembler;
    private IGenericDataStore genericDataStore;
    private ISummitEventDataStore summitEventDataStore;

    @Inject
    public TrackListInteractor(IDTOAssembler dtoAssembler, IGenericDataStore genericDataStore, ISummitEventDataStore summitEventDataStore) {
        super(dtoAssembler);
        this.genericDataStore     = genericDataStore;
        this.summitEventDataStore = summitEventDataStore;
    }

    @Override
    public List<TrackDTO> getTracks(List<Integer> trackGroups) {
        List<Track> tracks              = genericDataStore.getAllLocal(Track.class, new String[] { "name" }, new Sort[] { Sort.ASCENDING });
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
