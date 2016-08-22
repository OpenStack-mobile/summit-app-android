package org.openstack.android.summit.modules.speakers_list.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;
import org.openstack.android.summit.common.user_interface.PersonItemView;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public interface ISpeakerListPresenter extends IBasePresenter<ISpeakerListView> {

    void setView(ISpeakerListView speakerListFragment);

    void loadData();

    void buildItem(PersonItemView personItemView, int position);

    int getObjectsPerPage();

    void showSpeakerProfile(int position);
}
