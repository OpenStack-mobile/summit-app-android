package org.openstack.android.summit.common.data_access.data_polling;

import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.ITrackGroupDataStore;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.TrackGroup;

/**
 * Created by Claudio Redi on 3/4/2016.
 */
public class TrackGroupDataUpdateStrategy extends DataUpdateStrategy {
    private ITrackGroupDataStore trackGroupDataStore;

    public TrackGroupDataUpdateStrategy(IGenericDataStore genericDataStore, ITrackGroupDataStore trackGroupDataStore) {
        super(genericDataStore);
        this.trackGroupDataStore = trackGroupDataStore;
    }

    @Override
    public void process(DataUpdate dataUpdate) {
        int trackGroupId = ((TrackGroup)dataUpdate.getEntity()).getId();
        trackGroupDataStore.removeTrackGroupFromTracksLocal(trackGroupId);
        super.process(dataUpdate);
    }
}
