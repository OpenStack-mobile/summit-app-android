package org.openstack.android.openstacksummit.common.entities;

import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class EventType extends RealmObject implements INamedEntity {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
