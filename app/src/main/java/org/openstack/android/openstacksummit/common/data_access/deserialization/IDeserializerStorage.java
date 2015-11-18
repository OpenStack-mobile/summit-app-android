package org.openstack.android.openstacksummit.common.data_access.deserialization;

import org.openstack.android.openstacksummit.common.entities.IEntity;

import java.util.List;

/**
 * Created by Claudio Redi on 11/12/2015.
 */
public interface IDeserializerStorage {
    <T extends IEntity> void add(T entity, Class<T> type);

    <T extends IEntity> T get(int id, Class<T> type);

    <T extends IEntity> List<T> getAll(Class<T> type);

    <T extends IEntity> Boolean exist(T entity, Class<T> type);

    <T extends IEntity> Boolean exist(int entityId, Class<T> type);

    void clear();
}
