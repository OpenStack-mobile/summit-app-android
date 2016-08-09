package org.openstack.android.summit.modules.event_detail.user_interface;

import android.os.Bundle;
import org.openstack.android.summit.R;
import org.openstack.android.summit.modules.venue_detail.user_interface.AbstractVenueDetailFragment;

/**
 * Created by sebastian on 8/9/2016.
 */
public class VenueRoomDetailFragment extends AbstractVenueDetailFragment<IVenueRoomDetailPresenter> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        setTitle(getResources().getString(R.string.location));
        super.onResume();
    }
}
