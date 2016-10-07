package org.openstack.android.summit.common.data_access.deserialization;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.entities.IEntity;
import org.openstack.android.summit.common.utils.RealmFactory;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/12/2015.
 */
public class GenericDeserializer implements IGenericDeserializer {

    public GenericDeserializer() {
    }

    @Override
    public <T extends RealmObject & IEntity> T deserialize(final String jsonString, final Class<T> type) {

        try {
            return RealmFactory.transaction(new RealmFactory.IRealmCallback<T>() {
                @Override
                public T callback(Realm session) throws Exception {

                    return session.createOrUpdateObjectFromJson(type, jsonString);
                }
            });
        }
        catch (Exception ex){
            Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
            Crashlytics.logException(ex);

        }
        return null;
    }
}
