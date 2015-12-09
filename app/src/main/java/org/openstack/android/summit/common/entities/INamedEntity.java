package org.openstack.android.summit.common.entities;

/**
 * Created by Claudio Redi on 11/5/2015.
 */
public interface INamedEntity extends IEntity {
    String getName();
    void setName(String name);
}
