package org.openstack.android.summit.common.entities;

/**
 * Created by smarcet on 1/30/17.
 */

public interface IPresentation extends ISummitEvent {

    void setEvent(ISummitEvent event);

    ISummitEvent getEvent();
}
