package org.openstack.android.summit.modules.venue_detail.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.R;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public class VenueDetailFragment extends AbstractVenueDetailFragment<IVenueDetailPresenter> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        presenter.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        setTitle(getResources().getString(R.string.venue));
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }
}
