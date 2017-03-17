package org.openstack.android.summit.common.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by smarcet on 3/17/17.
 */

public class Organization extends RealmObject implements INamedEntity {

    @PrimaryKey
    private int id;

    private String name;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
