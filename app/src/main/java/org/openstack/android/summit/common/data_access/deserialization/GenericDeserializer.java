package org.openstack.android.summit.common.data_access.deserialization;

import com.alibaba.fastjson.JSON;

import org.openstack.android.summit.common.entities.IEntity;

import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/12/2015.
 */
public class GenericDeserializer implements IGenericDeserializer {
    IDeserializerStorage deserializerStorage;

    public GenericDeserializer(IDeserializerStorage deserializerStorage) {
        this.deserializerStorage = deserializerStorage;
    }

    @Override
    public <T extends RealmObject & IEntity> T deserialize(String jsonString, Class<T> type) {
        T entity = JSON.parseObject(jsonString, type);
        deserializerStorage.add(entity, type);
        return entity;
    }
}
