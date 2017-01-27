package org.openstack.android.summit.modules.speaker_presentations.business_logic;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.business_logic.ScheduleInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class SpeakerPresentationsInteractor extends ScheduleInteractor implements ISpeakerPresentationsInteractor {

    public SpeakerPresentationsInteractor(ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager, IPushNotificationsManager pushNotificationsManager, ISession session, ISummitSelector summitSelector) {
        super(summitEventDataStore, summitDataStore, summitAttendeeDataStore, dtoAssembler, securityManager, pushNotificationsManager, session, summitSelector);
    }

    @Override
    public int getCurrentMemberSpeakerId() {
        Member member = securityManager.getCurrentMember();
        return member.getSpeakerRole().getId();
    }

    @Override
    public List<ScheduleItemDTO> getSpeakerPresentations(int speakerId, DateTime startDate, DateTime endDate) {
        List<SummitEvent> speakerEvents = summitEventDataStore.getSpeakerEvents(speakerId, startDate, endDate);
        return createDTOList(speakerEvents, ScheduleItemDTO.class);
    }

    @Override
    public List<DateTime> getSpeakerPresentationScheduleDatesWithoutEvents(int speakerId, DateTime startDate, DateTime endDate) {
        ArrayList<DateTime> inactiveDates = new ArrayList<>();
        List<SummitEvent> events;

        while(startDate.isBefore(endDate)) {
            events = summitEventDataStore.getSpeakerEvents(
                    speakerId,
                    startDate.withTime(0, 0, 0, 0),
                    startDate.withTime(23, 59, 59, 999)
            );
            if (events.size() == 0) {
                inactiveDates.add(startDate);
            }
            startDate = startDate.plusDays(1);
        }

        return inactiveDates;
    }
}
