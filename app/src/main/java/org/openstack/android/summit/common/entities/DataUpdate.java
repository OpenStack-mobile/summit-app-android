package org.openstack.android.summit.common.entities;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Claudio Redi on 11/4/2015.
 */

enum DataOperation {
    Insert,
    Update,
    Delete
}

public class DataUpdate extends RealmObject {
    @PrimaryKey
    private int id;
    private int operation;
    private Date date;
    private String entityClassName;
    @Ignore
    private RealmObject entity;

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RealmObject getEntity() {
        return entity;
    }

    public void setEntity(RealmObject entity) {
        this.entity = entity;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
