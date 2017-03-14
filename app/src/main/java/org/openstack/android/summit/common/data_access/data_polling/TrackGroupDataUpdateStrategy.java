package org.openstack.android.summit.common.data_access.data_polling;

import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.ITrackGroupDataStore;
import org.openstack.android.summit.common.entities.DataUpdate;

/**
 * Created by Claudio Redi on 3/4/2016.
 */
public class TrackGroupDataUpdateStrategy extends DataUpdateStrategy {
    private ITrackGroupDataStore trackGroupDataStore;

    public TrackGroupDataUpdateStrategy(ITrackGroupDataStore trackGroupDataStore, ISummitSelector summitSelector) {
        super(summitSelector);
        this.trackGroupDataStore = trackGroupDataStore;
    }

    @Override
    public void process(DataUpdate dataUpdate) throws DataUpdateException {
        super.process(dataUpdate);
    }
}
