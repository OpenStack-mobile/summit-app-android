package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.summit.common.entities.IEntity;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/12/2015.
 */
public interface IDeserializer {
    <T extends RealmObject & IEntity> T deserialize(String jsonString, Class<T> type) throws JSONException, IllegalArgumentException;
    <T extends RealmObject & IEntity> List<T> deserializeList(String jsonString, Class<T> type) throws JSONException, IllegalArgumentException;
    <T extends RealmObject & IEntity> List<T> deserializePage(String jsonString, Class<T> type) throws JSONException, IllegalArgumentException;
}
