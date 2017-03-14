package org.openstack.android.summit.modules.favorites_schedule.business_logic;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.business_logic.IScheduleInteractor;

import java.util.Date;
import java.util.List;

/**
 * Created by smarcet on 3/14/17.
 */

public interface IFavoritesScheduleInteractor extends IScheduleInteractor {

    List<ScheduleItemDTO> getCurrentMemberFavoritesEvents(Date startDate, Date endDate);

    List<DateTime> getCurrentMemberFavoritesDatesWithoutEvents(DateTime startDate, DateTime endDate);
}
