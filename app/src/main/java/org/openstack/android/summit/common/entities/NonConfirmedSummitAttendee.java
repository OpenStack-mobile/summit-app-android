package org.openstack.android.summit.common.entities;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by Claudio Redi on 3/29/2016.
 */
public class NonConfirmedSummitAttendee extends RealmObject implements IEntity
{
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
