package org.openstack.android.summit.common.api;

/**
 * Created by smarcet on 11/9/16.
 */

public class SummitEventFeedbackRequest {

    final private Integer rate;
    final private String  note;

    public SummitEventFeedbackRequest(Integer rate, String note){
        this.rate = rate;
        this.note = note;
    }
}
