package org.openstack.android.summit.modules.event_detail.user_interface;

import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.common.user_interface.IScheduleableView;

import java.util.List;

/**
 * Created by Claudio Redi on 1/22/2016.
 */
public interface IEventDetailView extends IBaseView, IScheduleableView {
    void setName(String name);

    void setTrack(String track);

    void setDescription(String description);

    void setDate(String date);

    void setLocation(String location);

    void setCredentials(String credentials);

    void setLevel(String level);

    void setSponsors(String sponsors);

    void setSpeakers(List<PersonListItemDTO> speakers);

    void setScheduled(Boolean scheduled);

    void setTags(String tags);

    Boolean getScheduled();

    void setIsScheduledStatusVisible(Boolean isScheduledStatusVisible);
}
