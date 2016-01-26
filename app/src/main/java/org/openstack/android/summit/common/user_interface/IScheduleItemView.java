package org.openstack.android.summit.common.user_interface;

/**
 * Created by Claudio Redi on 12/28/2015.
 */
public interface IScheduleItemView extends IScheduleableView {
    String getName();

    void setName(String name);

    String getTime();

    void setTime(String time);

    String getSponsors();

    void setSponsors(String sponsors);

    String getEventType();

    void setEventType(String eventType);

    String getTrack();

    void setTrack(String track);

    void setColor(String color);

    Boolean getIsScheduledStatusVisible();

    void setIsScheduledStatusVisible(Boolean isScheduledStatusVisible);
}
