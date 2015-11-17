package org.openstack.android.openstacksummit.common.entities;

import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class Image extends RealmObject implements IEntity {
    private int id;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
