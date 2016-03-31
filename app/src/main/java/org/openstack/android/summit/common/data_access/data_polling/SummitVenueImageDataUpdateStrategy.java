package org.openstack.android.summit.common.data_access.data_polling;

<<<<<<< HEAD
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.IVenueDataStore;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.Image;
import org.openstack.android.summit.common.entities.Venue;

/**
 * Created by Claudio Redi on 3/28/2016.
 */
public class SummitVenueImageDataUpdateStrategy extends DataUpdateStrategy {
    IVenueDataStore venueDataStore;

    public SummitVenueImageDataUpdateStrategy(IGenericDataStore genericDataStore, IVenueDataStore venueDataStore) {
        super(genericDataStore);
        this.venueDataStore = venueDataStore;
    }

    @Override
    public void process(DataUpdate dataUpdate) throws DataUpdateException {
        switch (dataUpdate.getOperation()) {
            case DataOperation.Insert:

                JSONObject entityJSON = dataUpdate.getOriginalJSON().optJSONObject("entity");
                if (entityJSON != null) {
                    Integer venueId = entityJSON.optInt("location_id");
                    if (venueId != null) {
                        Venue venue = genericDataStore.getByIdLocal(venueId, Venue.class);
                        if (venue != null) {
                            Image image;
                            try {
                                image = (Image)dataUpdate.getEntity();
                            }
                            catch (Exception e) {
                                throw new DataUpdateException("Entity is not an image");
                            }

                            if (image != null) {
                                if (dataUpdate.getEntityClassName().equals("SummitLocationImage")) {
                                    venueDataStore.addImageToVenue(image, venue);
                                }
                                if (dataUpdate.getEntityClassName().equals("SummitLocationMap")) {
                                    venueDataStore.addMapToVenue(image, venue);
                                }
                            }
                            else {
                                throw new DataUpdateException("Entity is null");
                            }
                        }
                        else {
                            throw new DataUpdateException(String.format("Venue with id %d not found", venueId));
                        }
                    }
                    else {
                        throw new DataUpdateException("It wasn't possible to find location_id on data update json");
                    }
                }
                break;
            case DataOperation.Update:
                genericDataStore.saveOrUpdate((Image)dataUpdate.getEntity(), null, Image.class);
                break;
            case DataOperation.Delete:
                genericDataStore.delete(((Image)dataUpdate.getEntity()).getId(), null, Image.class);
                break;
        }
    }
}
=======
/**
 * Created by Claudio Redi on 3/23/2016.
 */
public class SummitVenueImageDataUpdateStrategy {
    /*private IVenueDataStore trackGroupDataStore;

    public TrackGroupDataUpdateStrategy(IGenericDataStore genericDataStore, ITrackGroupDataStore trackGroupDataStore) {
        super(genericDataStore);
        this.trackGroupDataStore = trackGroupDataStore;
    }

    @Override
    public void process(DataUpdate dataUpdate) {
        int trackGroupId = ((TrackGroup)dataUpdate.getEntity()).getId();
        trackGroupDataStore.removeTrackGroupFromTracksLocal(trackGroupId);
        super.process(dataUpdate);
    }*/
}
>>>>>>> ft-order-confirm
