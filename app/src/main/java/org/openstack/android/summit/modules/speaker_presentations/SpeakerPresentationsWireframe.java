package org.openstack.android.summit.modules.speaker_presentations;

import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.ScheduleWireframe;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.personal_schedule.IPersonalScheduleWireframe;

/**
 * Created by Claudio Redi on 1/28/2016.
 */
public class SpeakerPresentationsWireframe  extends ScheduleWireframe implements ISpeakerPresentationsWireframe {
    public SpeakerPresentationsWireframe(IEventDetailWireframe eventDetailWireframe, INavigationParametersStore navigationParametersStore) {
        super(eventDetailWireframe, navigationParametersStore);
    }
}
