package org.openstack.android.summit.common.data_access;

import com.fasterxml.jackson.core.util.VersionUtil;

import org.openstack.android.summit.common.entities.Image;
import org.openstack.android.summit.common.entities.Venue;

/**
 * Created by Claudio Redi on 3/28/2016.
 */
public class VenueDataStore extends GenericDataStore implements IVenueDataStore {

    @Override
    public void addImageToVenue(Image image, Venue venue) {
        try{
            realm.beginTransaction();
            venue.getImages().add(image);
            realm.commitTransaction();
        } catch (Exception e) {
            realm.cancelTransaction();
            throw e;
        }
    }

    @Override
    public void addMapToVenue(Image image, Venue venue) {
        try{
            realm.beginTransaction();
            Venue v = getByIdLocal(venue.getId(), Venue.class);
            v.getMaps().add(image);
            realm.commitTransaction();
        }
        catch (Exception e) {
            realm.cancelTransaction();
            throw e;
        }
    }
}
