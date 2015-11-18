package org.openstack.android.openstacksummit.common.data_access;

import org.openstack.android.openstacksummit.common.entities.Summit;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public class SummitDataStore extends GenericDataStore implements IDataStoreOperationListener<Summit>,ISummitDataStore {
    ISummitRemoteDataStore summitRemoteDataStore;

    @Inject
    public SummitDataStore(ISummitRemoteDataStore summitRemoteDataStore) {
        this.summitRemoteDataStore = summitRemoteDataStore;
    }

    @Override
    public void getActive(IDataStoreOperationListener<Summit> delegate) {
        List<Summit> summits = getaAllLocal(Summit.class);

        if (summits.size() > 0) {
            if (delegate != null) {
                delegate.onSuceedWithData(summits.get(0));
            }
        }
        else {
            summitRemoteDataStore.getActive(this);
        }
    }

    @Override
    public void onSuceedWithData(Summit data) {

    }

    @Override
    public void onSucceed() {

    }

    @Override
    public void onError(String message) {

    }

    /*public func getActive(completionBlock : (Summit?, NSError?) -> Void) {
        let summit = realm.objects(Summit.self).first
        if (summit != nil) {
            completionBlock(summit!, nil)
        }
        else {
            getActiveAsync(completionBlock)
        }
    }*/
}
