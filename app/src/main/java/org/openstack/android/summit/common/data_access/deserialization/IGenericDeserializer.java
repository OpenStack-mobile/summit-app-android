package org.openstack.android.summit.common.data_access.deserialization;

import org.openstack.android.summit.common.entities.IEntity;

import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/12/2015.
 */
public interface IGenericDeserializer {
    <T extends RealmObject & IEntity> T deserialize(String jsonString, Class<T> type);
}
