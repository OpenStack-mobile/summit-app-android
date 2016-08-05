package org.openstack.android.summit.modules.speaker_presentations.business_logic;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.business_logic.IScheduleInteractor;
import java.util.List;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public interface ISpeakerPresentationsInteractor extends IScheduleInteractor {
    int getCurrentMemberSpeakerId();

    List<ScheduleItemDTO> getSpeakerPresentations(int speakerId, DateTime startDate, DateTime endDate);

    List<DateTime> getSpeakerPresentationScheduleDatesWithoutEvents(int speakerId, DateTime startDate, DateTime endDate);
}
