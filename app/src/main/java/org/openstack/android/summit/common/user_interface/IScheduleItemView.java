package org.openstack.android.summit.common.user_interface;

/**
 * Created by Claudio Redi on 12/28/2015.
 */
public interface IScheduleItemView extends IScheduleableItem {

    void setName(String name);

    void setTime(String time);

    void setSponsors(String sponsors);

    void setEventType(String eventType);

    void setTrack(String track);

    void setColor(String color);

    void setLocation(String location);

    void showLocation(boolean show);

    void shouldShowFavoritesOption(boolean show);

    void shouldShowGoingToOption(boolean show);

    void shouldShowRSVPToOption(boolean show);

    void shouldShowUnRSVPToOption(boolean show);

    void setContextualMenu();

}
