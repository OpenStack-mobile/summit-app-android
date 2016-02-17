package org.openstack.android.summit.common.entities;

import com.alibaba.fastjson.annotation.JSONField;

import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/4/2015.
 */
public class Image extends RealmObject implements IEntity {
    private int id;
    private String imageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JSONField(name="image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @JSONField(name="image_url")
    public void setImageUrl(String url) {
        this.imageUrl = url;
    }
}
