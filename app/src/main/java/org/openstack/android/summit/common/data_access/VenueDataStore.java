package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.List;

public class VenueDataStore extends GenericDataStore implements IVenueDataStore {

    @Override
    public List<Venue> getInternalsBySummit(int summitId) {
        return RealmFactory.getSession().where(Venue.class).equalTo("summit.id", summitId).equalTo("isInternal", true).findAllSorted("name");
    }

    @Override
    public List<Venue> getExternalBySummit(int summitId) {
        return RealmFactory.getSession().where(Venue.class).equalTo("summit.id", summitId).equalTo("isInternal", false).findAllSorted("name");
    }

    @Override
    public List<Venue> getAllBySummit(int summitId) {
        return RealmFactory.getSession().where(Venue.class).equalTo("summit.id", summitId).findAllSorted("name");
    }
}