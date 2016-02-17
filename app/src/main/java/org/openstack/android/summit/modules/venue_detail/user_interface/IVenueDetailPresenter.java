package org.openstack.android.summit.modules.venue_detail.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;
import org.openstack.android.summit.common.user_interface.ISimpleListItemView;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public interface IVenueDetailPresenter extends IBasePresenter<IVenueDetailView> {
    void buildItem(ISimpleListItemView venueRoomListItem, int position);

    void showToMapIfApplies();
}
