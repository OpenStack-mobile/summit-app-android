package org.openstack.android.summit.common.user_interface;

/**
 * Created by Claudio Redi on 12/28/2015.
 */
public interface IScheduleItemView extends IScheduleableView {

    void setName(String name);

    void setTime(String time);

    void setSponsors(String sponsors);

    void setEventType(String eventType);

    void setTrack(String track);

    void setColor(String color);

    void setIsScheduledStatusVisible(Boolean isScheduledStatusVisible);

    void setLocation(String location);
}
