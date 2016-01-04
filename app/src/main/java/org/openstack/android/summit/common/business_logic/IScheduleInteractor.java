package org.openstack.android.summit.common.business_logic;

import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public interface IScheduleInteractor {
    void getActiveSummit(IInteractorAsyncOperationListener<SummitDTO> delegate);

    List<ScheduleItemDTO> getScheduleEvents(Date startDate, Date endDate, List<Integer> eventTypes, List<Integer> summitTypes, List<Integer> tracks, List<String> tags, List<String> levels);

    Boolean isEventScheduledByLoggedMember(int eventId);

    void addEventToLoggedInMemberSchedule(int id, InteractorAsyncOperationListener<Void> interactorOperationListener);

    void removeEventToLoggedInMemberSchedule(int id, InteractorAsyncOperationListener<Void> interactorOperationListener);
}
