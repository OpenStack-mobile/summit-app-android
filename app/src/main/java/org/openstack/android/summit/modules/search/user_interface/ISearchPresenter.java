package org.openstack.android.summit.modules.search.user_interface;

import org.openstack.android.summit.common.user_interface.IPersonItemView;
import org.openstack.android.summit.common.user_interface.IScheduleListPresenter;
import org.openstack.android.summit.common.user_interface.ISimpleListItemView;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public interface ISearchPresenter extends IScheduleListPresenter<ISearchView> {

    void search(String searchTerm);

    void showTrackSchedule(int position);

    void buildTrackItem(ISimpleListItemView simpleListItemView, int position);

    void buildSpeakerItem(IPersonItemView personItemView, int position);

    void showSpeakerProfile(int position);
}
