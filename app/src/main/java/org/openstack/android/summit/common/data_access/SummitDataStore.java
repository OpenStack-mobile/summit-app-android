package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.entities.Summit;

import java.util.List;

import javax.inject.Inject;

import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public class SummitDataStore extends GenericDataStore implements IDataStoreOperationListener<Summit>,ISummitDataStore {
    private ISummitRemoteDataStore summitRemoteDataStore;
    private IDataStoreOperationListener<Summit> delegate;

    @Inject
    public SummitDataStore(ISummitRemoteDataStore summitRemoteDataStore) {
        this.summitRemoteDataStore = summitRemoteDataStore;
    }

    @Override
    public void getActive() {
        List<Summit> summits = getaAllLocal(Summit.class);

        if (summits.size() > 0) {
            if (delegate != null) {
                delegate.onSuceedWithData(summits.get(0));
            }
        }
        else {

            DataStoreOperationListener<Summit> remoteDelegate = new DataStoreOperationListener<Summit>() {
                @Override
                public void onSuceedWithData(Summit data) {
                    try{
                        realm.beginTransaction();
                        Summit realmEntity = realm.copyToRealmOrUpdate(data);
                        realm.commitTransaction();
                        if (delegate != null) {
                            delegate.onSuceedWithData(realmEntity);
                        }
                    }
                    catch (Exception ex) {
                        delegate.onError(ex.getMessage());
                    }
                }

                @Override
                public void onError(String message) {
                    super.onError(message);
                }
            };

            summitRemoteDataStore.setDelegate(remoteDelegate);
            summitRemoteDataStore.getActive();
        }
    }

    @Override
    public void onSuceedWithData(Summit data) {
        delegate.onSuceedWithData(data);
    }

    @Override
    public void onSucceed() {

    }

    @Override
    public void onError(String message) {
        delegate.onError(message);
    }

    public IDataStoreOperationListener<Summit> getDelegate() {
        return delegate;
    }

    public void setDelegate(IDataStoreOperationListener<Summit> delegate) {
        this.delegate = delegate;
    }
}
