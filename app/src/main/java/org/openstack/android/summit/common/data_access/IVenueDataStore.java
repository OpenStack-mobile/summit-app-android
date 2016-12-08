package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Venue;

import java.util.List;

/**
 * Created by Claudio Redi on 3/28/2016.
 */
public interface IVenueDataStore extends IGenericDataStore {

    List<Venue> getInternalsBySummit(int summitId);

    List<Venue> getExternalBySummit(int summitId);

    List<Venue> getAllBySummit(int summitId);
}
