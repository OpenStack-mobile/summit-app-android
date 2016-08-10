package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Image;
import org.openstack.android.summit.common.entities.Venue;

/**
 * Created by Claudio Redi on 3/28/2016.
 */
public interface IVenueDataStore extends IGenericDataStore {

    void addImageToVenue(Image image, Venue venue);

    void addMapToVenue(Image image, Venue venue);
}
