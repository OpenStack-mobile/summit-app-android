package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.entities.Summit;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public class SummitRemoteDataStore implements ISummitRemoteDataStore {
    private IDeserializer deserializer;
    private IDataStoreOperationListener<Summit> delegate;

    public SummitRemoteDataStore(IDeserializer deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public void getActive(IDataStoreOperationListener<Summit> delegate) {

        try {
            Summit summit = deserializer.deserialize(Constants.summitJSON, Summit.class);
            delegate.onSuceedWithData(summit);
        } catch (Exception e) {
            delegate.onError(e.getMessage());
        }
    }

    public IDataStoreOperationListener<Summit> getDelegate() {
        return delegate;
    }

    public void setDelegate(IDataStoreOperationListener<Summit> delegate) {
        this.delegate = delegate;
    }
}
