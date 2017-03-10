package org.openstack.android.summit.modules.speakers_list.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;
import java.util.HashMap;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public interface ISpeakerListPresenter extends IBasePresenter<ISpeakerListView> {

    void setView(ISpeakerListView speakerListFragment);

    void loadData();

    void buildItem(SpeakerListAdapter.SpeakerItemViewHolder speakerItemView, int position);

    void showSpeakerProfile(int position);

    public HashMap<String, Integer> createMapIndex();
}
