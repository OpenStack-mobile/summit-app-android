package org.openstack.android.summit.modules.venue_list.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;
import org.openstack.android.summit.common.user_interface.SimpleListItemView;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public interface IVenueListPresenter extends IBasePresenter<IVenueListView> {
    void showVenueDetail(int position);

    void buildItem(IVenueListItemView venueListItemView, int position);
}
