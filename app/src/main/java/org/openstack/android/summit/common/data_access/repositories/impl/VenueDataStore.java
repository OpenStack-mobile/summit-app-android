package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.repositories.IVenueDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.List;

public class VenueDataStore extends GenericDataStore<Venue> implements IVenueDataStore {

    public VenueDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(Venue.class, saveOrUpdateStrategy, deleteStrategy);
    }

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