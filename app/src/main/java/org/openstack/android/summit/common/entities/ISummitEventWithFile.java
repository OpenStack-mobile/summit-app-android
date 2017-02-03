package org.openstack.android.summit.common.entities;

/**
 * Created by smarcet on 2/3/17.
 */

public interface ISummitEventWithFile extends ISummitEvent {

    String getAttachment();

    void setAttachment(String attachment);

    void setEvent(ISummitEvent event);

    ISummitEvent getEvent();
}
