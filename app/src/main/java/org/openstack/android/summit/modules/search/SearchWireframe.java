package org.openstack.android.summit.modules.search;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.modules.search.user_interface.SearchFragment;
import org.openstack.android.summit.modules.track_schedule.ITrackScheduleWireframe;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public class SearchWireframe extends BaseWireframe implements ISearchWireframe {

    private ITrackScheduleWireframe trackScheduleWireframe;

    @Inject
    public SearchWireframe(ITrackScheduleWireframe trackScheduleWireframe, INavigationParametersStore navigationParametersStore) {
        super(navigationParametersStore);
        this.trackScheduleWireframe = trackScheduleWireframe;
    }

    @Override
    public void presentSearchView(String searchTerm, FragmentActivity context) {
        navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_SEARCH_TERM, searchTerm);
        SearchFragment searchFragment = new SearchFragment();
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frame_layout_content, searchFragment)
                .commit();
    }

    @Override
    public void showTrackSchedule(NamedDTO track, FragmentActivity activity) {
        trackScheduleWireframe.presentTrackScheduleView(track, activity);
    }
}
