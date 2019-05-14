package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.repositories.ITrackDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.List;

import io.realm.Sort;


public class TrackDataStore extends GenericDataStore<Track> implements ITrackDataStore {

    public TrackDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(Track.class, saveOrUpdateStrategy, deleteStrategy);
    }

    @Override
    public List<Track> getAllOrderedByName(int summitId) {
        return RealmFactory.getSession().where(Track.class).equalTo("summit.id", summitId)
                .sort("name", Sort.ASCENDING).findAll();
    }

}
