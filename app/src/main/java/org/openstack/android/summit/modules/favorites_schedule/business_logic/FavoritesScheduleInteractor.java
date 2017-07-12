package org.openstack.android.summit.modules.favorites_schedule.business_logic;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.ScheduleInteractor;
import org.openstack.android.summit.common.data_access.repositories.IMemberDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Sort;

/**
 * Created by smarcet on 3/14/17.
 */

public class FavoritesScheduleInteractor
        extends ScheduleInteractor
        implements IFavoritesScheduleInteractor {

    public FavoritesScheduleInteractor
    (
            IMemberDataStore memberDataStore,
            ISummitEventDataStore summitEventDataStore,
            ISummitDataStore summitDataStore,
            IDTOAssembler dtoAssembler,
            ISecurityManager securityManager,
            IPushNotificationsManager pushNotificationsManager,
            ISession session,
            ISummitSelector summitSelector
    )
    {
        super(summitEventDataStore, summitDataStore, memberDataStore, dtoAssembler, securityManager, pushNotificationsManager, session, summitSelector);
    }

    @Override
    public List<ScheduleItemDTO> getCurrentMemberFavoritesEvents(Date startDate, Date endDate) {
        Member member              = securityManager.getCurrentMember();
        if(member == null) return new ArrayList<>();

        List<SummitEvent> favoriteEvents = member.getFavoriteEvents()
                    .where()
                    .greaterThanOrEqualTo("start", startDate)
                    .lessThanOrEqualTo("end", endDate)
                    .findAllSorted(new String[]{"start", "end", "name"}, new Sort[]{Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING});

        return postProcessScheduleEventList(createDTOList(favoriteEvents, ScheduleItemDTO.class));

    }

    @Override
    public List<DateTime> getCurrentMemberFavoritesDatesWithoutEvents(DateTime startDate, DateTime endDate) {
        ArrayList<DateTime> inactiveDates = new ArrayList<>();
        List<ScheduleItemDTO> events;

        while(startDate.isBefore(endDate)) {
            events = getCurrentMemberFavoritesEvents(
                    startDate.withTime(0, 0, 0, 0).toDate(),
                    startDate.withTime(23, 59, 59, 999).toDate()
            );
            if (events.size() == 0) {
                inactiveDates.add(startDate);
            }
            startDate = startDate.plusDays(1);
        }

        return inactiveDates;

    }
}
