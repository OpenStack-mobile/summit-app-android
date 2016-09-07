package org.openstack.android.summit.common.data_access.deserialization;

import org.openstack.android.summit.common.entities.IEntity;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/12/2015.
 */
public interface IDeserializerStorage {

    <T extends RealmObject & IEntity> void add(T entity, Class<T> type);

    <T extends RealmObject & IEntity> T get(int id, Class<T> type);

    <T extends RealmObject & IEntity> List<T> getAll(Class<T> type);

    <T extends RealmObject & IEntity> Boolean exist(T entity, Class<T> type);

    <T extends RealmObject & IEntity> Boolean exist(int entityId, Class<T> type);

    boolean cancelClear();

    void enableClear();

    boolean canClear();

    void clear();
}
