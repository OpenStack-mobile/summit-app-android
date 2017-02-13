package org.openstack.android.summit.modules.teams_notifications_inbox.user_interface;

import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.teams_notifications_inbox.ITeamNotificationsWireframe;
import org.openstack.android.summit.modules.teams_notifications_inbox.business_logic.ITeamsListInteractor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by smarcet on 2/13/17.
 */

public class TeamsListPresenter
        extends BasePresenter<ITeamsListView, ITeamsListInteractor, ITeamNotificationsWireframe>
        implements  ITeamsListPresenter {

    public TeamsListPresenter(ITeamsListInteractor interactor, ITeamNotificationsWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void loadData() {
        interactor.loadMyTeams()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).
                subscribe(list -> view.setData(list));
    }
}
