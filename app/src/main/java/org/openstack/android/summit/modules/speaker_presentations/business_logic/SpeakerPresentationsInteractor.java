package org.openstack.android.summit.modules.speaker_presentations.business_logic;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.business_logic.ScheduleInteractor;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.Date;
import java.util.List;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class SpeakerPresentationsInteractor extends ScheduleInteractor implements ISpeakerPresentationsInteractor {
    public SpeakerPresentationsInteractor(ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager, IDataUpdatePoller dataUpdatePoller) {
        super(summitEventDataStore, summitDataStore, summitAttendeeDataStore, dtoAssembler, securityManager, dataUpdatePoller);
    }

    @Override
    public int getCurrentMemberSpeakerId() {
        Member member = securityManager.getCurrentMember();
        return member.getSpeakerRole().getId();
    }

    @Override
    public List<ScheduleItemDTO> getSpeakerPresentations(int speakerId, Date startDate, Date endDate) {
        List<SummitEvent> speakerEvents = summitEventDataStore.getSpeakerEvents(speakerId, startDate, endDate);
        List<ScheduleItemDTO> dtos = createDTOList(speakerEvents, ScheduleItemDTO.class);
        return dtos;
    }
}
