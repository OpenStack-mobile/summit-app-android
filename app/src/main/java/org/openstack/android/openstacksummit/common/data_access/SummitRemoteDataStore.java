package org.openstack.android.openstacksummit.common.data_access;

import org.json.JSONException;
import org.openstack.android.openstacksummit.common.Constants;
import org.openstack.android.openstacksummit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.openstacksummit.common.entities.Summit;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public class SummitRemoteDataStore implements ISummitRemoteDataStore {

    IDeserializer deserializer;

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
}
