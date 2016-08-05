package org.openstack.android.summit.common.business_logic;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import java.util.List;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public interface IScheduleInteractor extends IScheduleableInteractor {

    void getActiveSummit(IInteractorAsyncOperationListener<SummitDTO> delegate);

    List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, List<Integer> eventTypes, List<Integer> summitTypes, List<Integer> trackGroups, List<Integer> tracks, List<String> tags, List<String> levels);

    void subscribeToPushChannelsUsingContextIfNotDoneAlready();

    List<DateTime> getDatesWithoutEvents(DateTime startDate, DateTime endDate, List<Integer> eventTypes, List<Integer> summitTypes, List<Integer> trackGroups, List<Integer> tracks, List<String> tags, List<String> levels);

    boolean eventExist(int id);

    SummitDTO getLocalActiveSummit();

    boolean isDataLoaded();
}
