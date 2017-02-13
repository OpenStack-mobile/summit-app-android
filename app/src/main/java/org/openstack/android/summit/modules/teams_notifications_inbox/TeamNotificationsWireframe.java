package org.openstack.android.summit.modules.teams_notifications_inbox;



import android.support.v4.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.teams_notifications_inbox.user_interface.TeamsListFragment;

/**
 * Created by smarcet on 2/13/17.
 */

public class TeamNotificationsWireframe
        extends BaseWireframe
        implements ITeamNotificationsWireframe {

    public TeamNotificationsWireframe(INavigationParametersStore navigationParametersStore) {
        super(navigationParametersStore);
    }

    @Override
    public void presentTeamsListView(IBaseView context) {
        TeamsListFragment teamsListFragment = new TeamsListFragment();
        FragmentManager fragmentManager = context.getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, teamsListFragment, "nav_teams")
                .addToBackStack("nav_teams")
                .commitAllowingStateLoss();
    }

}
