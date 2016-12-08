package org.openstack.android.summit.common.data_access.data_polling;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.IVenueDataStore;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.Image;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import io.realm.Realm;

/**
 * Created by Claudio Redi on 3/28/2016.
 */
public class SummitVenueImageDataUpdateStrategy extends DataUpdateStrategy {
    IVenueDataStore venueDataStore;

    public SummitVenueImageDataUpdateStrategy(IGenericDataStore genericDataStore, IVenueDataStore venueDataStore, ISummitSelector summitSelector) {
        super(genericDataStore, summitSelector);
        this.venueDataStore = venueDataStore;
    }

    @Override
    public void process(final DataUpdate dataUpdate) throws DataUpdateException {
        switch (dataUpdate.getOperation()) {
            case DataOperation.Insert:
                try {
                    RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                        @Override
                        public Void callback(Realm session) throws Exception {

                            JSONObject entityJSON = dataUpdate.getOriginalJSON().optJSONObject("entity");
                            if (entityJSON == null) return Void.getInstance();
                            Integer venueId = entityJSON.optInt("location_id");
                            if (venueId == null)
                                throw new DataUpdateException("It wasn't possible to find location_id on data update json");

                            Venue venue = genericDataStore.getByIdLocal(venueId, Venue.class);

                            if (venue == null)
                                throw new DataUpdateException(String.format("Venue with id %d not found", venueId));

                            Image image = (Image) dataUpdate.getEntity();

                            if (image == null)
                                throw new DataUpdateException("Entity is null");

                            if (dataUpdate.getEntityClassName().equals("SummitLocationImage")) {
                               venue.getImages().add(image);
                            }
                            if (dataUpdate.getEntityClassName().equals("SummitLocationMap")) {
                                venue.getMaps().add(image);
                            }

                            return Void.getInstance();
                        }
                    });
                }
                catch (Exception ex){
                    Log.e(Constants.LOG_TAG, ex.getMessage());
                    Crashlytics.logException(ex);
                    throw new DataUpdateException(ex);
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