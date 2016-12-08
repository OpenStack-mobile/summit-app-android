package org.openstack.android.summit.common.data_access.data_polling;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.entities.VenueFloor;
import org.openstack.android.summit.common.entities.VenueRoom;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import io.realm.Realm;

/**
 * Created by sebastian on 9/1/2016.
 */
public class VenueLocationsDataUpdateStrategy extends DataUpdateStrategy {

    public VenueLocationsDataUpdateStrategy(IGenericDataStore genericDataStore, ISummitSelector summitSelector) {
        super(genericDataStore, summitSelector);
    }

    @Override
    public void process(final DataUpdate dataUpdate) throws DataUpdateException {
        final String className = dataUpdate.getEntityClassName();
        switch (dataUpdate.getOperation()) {
            case DataOperation.Insert:
                try {

                    RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                        @Override
                        public Void callback(Realm session) throws Exception {

                            JSONObject entityJSON = dataUpdate.getOriginalJSON().optJSONObject("entity");
                            if (entityJSON == null)
                                throw new DataUpdateException("missing entity from data update");

                            Integer venueId = entityJSON.optInt("venue_id", 0);
                            Integer floorId = 0;

                            if (entityJSON.has("floor")) {
                                JSONObject floorJSON = entityJSON.getJSONObject("floor");
                                floorId = floorJSON.optInt("id");
                            }

                            if (venueId == 0)
                                throw new DataUpdateException("It wasn't possible to find venue_id on data update json");

                            Venue managedVenue      = genericDataStore.getByIdLocal(venueId, Venue.class);
                            VenueFloor managedFloor = genericDataStore.getByIdLocal(floorId, VenueFloor.class);

                            if (managedVenue == null)
                                throw new DataUpdateException(String.format("Venue with id %d not found", venueId));

                            if (className.equals("SummitVenueFloor")) {
                                VenueFloor floor = (VenueFloor) dataUpdate.getEntity();
                                floor.setVenue(managedVenue);
                                managedVenue.getFloors().add(floor);
                            }

                            if (className.equals("SummitVenueRoom")) {
                                VenueRoom room = (VenueRoom) dataUpdate.getEntity();
                                room.setVenue(managedVenue);
                                if (managedFloor != null) {
                                    room.setFloor(managedFloor);
                                    managedFloor.getRooms().add(room);
                                }
                            }
                            return Void.getInstance();
                        }
                    });

                } catch (Exception ex) {
                    Log.e(Constants.LOG_TAG, ex.getMessage());
                    Crashlytics.logException(ex);
                    throw new DataUpdateException(ex);
                }
                break;
            case DataOperation.Update:
                if (className.equals("SummitVenueFloor")) {
                    genericDataStore.saveOrUpdate((VenueFloor) dataUpdate.getEntity(), null, VenueFloor.class);
                }
                if (className.equals("SummitVenueRoom")) {
                    genericDataStore.saveOrUpdate((VenueRoom) dataUpdate.getEntity(), null, VenueRoom.class);
                }
                break;
            case DataOperation.Delete:
                if (className.equals("SummitVenueFloor")) {
                    genericDataStore.delete(((VenueFloor) dataUpdate.getEntity()).getId(), null, VenueFloor.class);
                }
                if (className.equals("SummitVenueRoom")) {
                    genericDataStore.delete(((VenueRoom) dataUpdate.getEntity()).getId(), null, VenueRoom.class);
                }

                break;
        }
    }
}
