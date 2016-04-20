package org.openstack.android.summit.common.entities;

import com.alibaba.fastjson.annotation.JSONField;

import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 4/20/2016.
 */
public class SummitEventAverageFeedback extends RealmObject implements IEntity {
    private int id;
    private double averageRate;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @JSONField(name="avg_feedback_rate")
    public double getAverageRate() {
        return averageRate;
    }

    @JSONField(name="avg_feedback_rate")
    public void setAverageRate(double averageRate) {
        this.averageRate = averageRate;
    }
}
