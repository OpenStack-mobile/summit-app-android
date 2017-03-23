package org.openstack.android.summit.modules.search;

import org.openstack.android.summit.common.IBaseWireframe;
import org.openstack.android.summit.common.IScheduleWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public interface ISearchWireframe extends IBaseWireframe, IScheduleWireframe
{
    void presentSearchView(String searchTerm, IBaseView context);

    void showTrackSchedule(int trackId, IBaseView context);

    void showSpeakerProfile(int speakerId, IBaseView context);
}
