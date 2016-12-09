package org.openstack.android.summit.modules.venue_list.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public interface IVenueListPresenter extends IBasePresenter<IVenueListView> {

    void showInternalVenueDetail(int position);

    void buildInternalVenueItem(IVenueListItemView venueListItemView, int position);

    void showExternalVenueDetail(int position);

    void buildExternalVenueItem(IVenueListItemView venueListItemView, int position);
}
