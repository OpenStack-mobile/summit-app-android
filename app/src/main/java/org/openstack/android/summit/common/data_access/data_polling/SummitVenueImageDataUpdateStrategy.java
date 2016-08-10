package org.openstack.android.summit.common.data_access.data_polling;

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
                if (entityJSON == null) return;
                Integer venueId = entityJSON.optInt("location_id");

                if (venueId == null)
                    throw new DataUpdateException("It wasn't possible to find location_id on data update json");

                Venue venue = genericDataStore.getByIdLocal(venueId, Venue.class);

                if (venue == null)
                    throw new DataUpdateException(String.format("Venue with id %d not found", venueId));

                Image image;
                try {
                    image = (Image) dataUpdate.getEntity();
                } catch (Exception e) {
                    throw new DataUpdateException("Entity is not an image");
                }

                if (image == null)
                    throw new DataUpdateException("Entity is null");

                if (dataUpdate.getEntityClassName().equals("SummitLocationImage")) {
                    venueDataStore.addImageToVenue(image, venue);
                }
                if (dataUpdate.getEntityClassName().equals("SummitLocationMap")) {
                    venueDataStore.addMapToVenue(image, venue);
                }
                break;
            case DataOperation.Update:
                genericDataStore.saveOrUpdate((Image) dataUpdate.getEntity(), null, Image.class);
                break;
            case DataOperation.Delete:
                genericDataStore.delete(((Image) dataUpdate.getEntity()).getId(), null, Image.class);
                break;
        }
    }
}