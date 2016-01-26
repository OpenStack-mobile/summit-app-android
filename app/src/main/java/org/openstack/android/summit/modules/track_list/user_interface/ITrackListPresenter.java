package org.openstack.android.summit.modules.track_list.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;
import org.openstack.android.summit.common.user_interface.ISimpleListItemView;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public interface ITrackListPresenter extends IBasePresenter<ITrackListView> {

    void showTrackEvents(int position);

    void buildItem(ISimpleListItemView trackListItemView, int position);

    void reloadData();
}
