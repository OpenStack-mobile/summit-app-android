package org.openstack.android.summit.common.data_access;
import org.openstack.android.summit.common.entities.Image;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.utils.RealmFactory;

public class VenueDataStore extends GenericDataStore implements IVenueDataStore {

    @Override
    public void addImageToVenue(Image image, Venue venue) {
        try {
            RealmFactory.getSession().beginTransaction();
            venue.getImages().add(image);
            RealmFactory.getSession().commitTransaction();
        } catch (Exception e) {
            RealmFactory.getSession().cancelTransaction();
            throw e;
        }
    }

    @Override
    public void addMapToVenue(Image image, Venue venue) {
        try {
            RealmFactory.getSession().beginTransaction();
            Venue v = getByIdLocal(venue.getId(), Venue.class);
            v.getMaps().add(image);
            RealmFactory.getSession().commitTransaction();
        } catch (Exception e) {
            RealmFactory.getSession().cancelTransaction();
            throw e;
        }
    }
}