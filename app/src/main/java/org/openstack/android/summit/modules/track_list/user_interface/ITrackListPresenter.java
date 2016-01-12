package org.openstack.android.summit.modules.track_list.user_interface;

import org.openstack.android.summit.common.user_interface.IPresenter;
import org.openstack.android.summit.common.user_interface.ISimpleListItemView;
import org.openstack.android.summit.modules.level_list.user_interface.LevelListFragment;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public interface ITrackListPresenter extends IPresenter<TrackListFragment> {
    void setView(TrackListFragment view);

    void showTrackEvents(int position);

    void buildItem(ISimpleListItemView trackListItemView, int position);
}
