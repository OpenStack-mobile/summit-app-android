package org.openstack.android.summit.common.business_logic;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import java.util.List;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public interface IScheduleInteractor extends IScheduleableInteractor {

    List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, List<Integer> eventTypes, List<Integer> summitTypes, List<Integer> trackGroups, List<Integer> tracks, List<String> tags, List<String> levels, List<Integer> venues);

    List<DateTime> getDatesWithoutEvents(DateTime startDate, DateTime endDate, List<Integer> eventTypes, List<Integer> summitTypes, List<Integer> trackGroups, List<Integer> tracks, List<String> tags, List<String> levels, List<Integer> venues);

    boolean eventExist(int id);
}
