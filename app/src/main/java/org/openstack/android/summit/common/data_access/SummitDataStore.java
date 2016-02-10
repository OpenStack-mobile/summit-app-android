package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.entities.Summit;

import java.util.List;

import javax.inject.Inject;

import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public class SummitDataStore extends GenericDataStore implements ISummitDataStore {
    private ISummitRemoteDataStore summitRemoteDataStore;

    @Inject
    public SummitDataStore(ISummitRemoteDataStore summitRemoteDataStore) {
        this.summitRemoteDataStore = summitRemoteDataStore;
    }

    @Override
    public void getActive(final IDataStoreOperationListener<Summit> dataStoreOperationListener) {
        List<Summit> summits = getaAllLocal(Summit.class);

        if (summits.size() > 0) {
            if (dataStoreOperationListener != null) {
                dataStoreOperationListener.onSuceedWithSingleData(summits.get(0));
            }
        }
        else {

            DataStoreOperationListener<Summit> remoteDelegate = new DataStoreOperationListener<Summit>() {
                @Override
                public void onSuceedWithSingleData(Summit data) {
                    try{
                        realm.beginTransaction();
                        Summit realmEntity = realm.copyToRealmOrUpdate(data);
                        realm.commitTransaction();
                        if (dataStoreOperationListener != null) {
                            dataStoreOperationListener.onSuceedWithSingleData(realmEntity);
                        }
                    }
                    catch (Exception ex) {
                        dataStoreOperationListener.onError(ex.getMessage());
                    }
                }

                @Override
                public void onError(String message) {
                    dataStoreOperationListener.onError(message);
                }
            };

            summitRemoteDataStore.getActive(remoteDelegate);
        }
    }

    @Override
    public Summit getActiveLocal() {
        List<Summit> summits = getaAllLocal(Summit.class);
        return summits.size() > 0 ? summits.get(0) : null;
    }
}
