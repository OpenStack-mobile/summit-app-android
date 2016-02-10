package org.openstack.android.summit.common.data_access.deserialization;

import org.openstack.android.summit.common.entities.IEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/11/2015.
 */
public class DeserializerStorage implements IDeserializerStorage {

    @Inject
    public DeserializerStorage(Realm realm) {
        this.realm = realm;
    }

    Realm realm;

    Map<Class, Map<Integer, IEntity>> deserializedEntityDictionary = new HashMap<Class, Map<Integer, IEntity>>();

    @Override
    public <T extends RealmObject & IEntity> void add(T entity, Class<T> type) {
        if (!deserializedEntityDictionary.containsKey(type)) {
            deserializedEntityDictionary.put(type, new HashMap<Integer, IEntity>());
        }
        deserializedEntityDictionary.get(type).put(entity.getId(), entity);
    }

    @Override
    public <T extends RealmObject & IEntity> T get(int id, Class<T> type) {
        T entity = null;
        if (deserializedEntityDictionary.containsKey(type) && deserializedEntityDictionary.get(type).containsKey(id)) {
            entity = (T)deserializedEntityDictionary.get(type).get(id);
        }

        if (entity == null) {
            entity = realm.where(type).equalTo("id", id).findFirst();
        }
        return entity;
    }

    @Override
    public <T extends RealmObject & IEntity> List<T> getAll(Class<T> type) {
        List<T> list = new ArrayList<T>();
        if (deserializedEntityDictionary.containsKey(type)) {
            for (IEntity entity : deserializedEntityDictionary.get(type).values()) {
                list.add((T)entity);
            }
        }
        else {
            list = realm.where(type).findAll();
        }
        return list;
    }

    @Override
    public <T extends RealmObject & IEntity> Boolean exist(int entityId, Class<T> type) {
        return (deserializedEntityDictionary.containsKey(type) &&
                deserializedEntityDictionary.get(type).containsKey(entityId)) ||
                realm.where(type).equalTo("id", entityId).count() > 0;
    }

    @Override
    public <T extends RealmObject & IEntity> Boolean exist(T entity, Class<T> type) {
        return exist(entity.getId(), type);
    }

    @Override
    public void clear() {
        deserializedEntityDictionary.clear();
    }
}
