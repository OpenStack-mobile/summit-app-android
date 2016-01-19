package org.openstack.android.summit.modules.search.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;
import org.openstack.android.summit.common.user_interface.IPersonItemView;
import org.openstack.android.summit.common.user_interface.IScheduleItemView;
import org.openstack.android.summit.common.user_interface.ISimpleListItemView;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public interface ISearchPresenter extends IBasePresenter<SearchFragment> {
    void setSearchTerm(String searchTerm);

    void buildScheduleItem(IScheduleItemView scheduleItemView, int position);

    void toggleScheduleStatus(IScheduleItemView scheduleItemView, int position);

    void showEventDetail(int position);

    void search(String searchTerm);

    void showTrackSchedule(int position);

    void buildTrackItem(ISimpleListItemView simpleListItemView, int position);

    void buildSpeakerItem(IPersonItemView personItemView, int position);

    void showSpeakerProfile(int position);
}
