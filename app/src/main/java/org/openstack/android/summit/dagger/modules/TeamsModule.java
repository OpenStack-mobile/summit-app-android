package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.remote.ITeamRemoteDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.modules.teams_notifications_inbox.ITeamNotificationsWireframe;
import org.openstack.android.summit.modules.teams_notifications_inbox.TeamNotificationsWireframe;
import org.openstack.android.summit.modules.teams_notifications_inbox.business_logic.ITeamsListInteractor;
import org.openstack.android.summit.modules.teams_notifications_inbox.business_logic.TeamsListInteractor;
import org.openstack.android.summit.modules.teams_notifications_inbox.user_interface.ITeamsListPresenter;
import org.openstack.android.summit.modules.teams_notifications_inbox.user_interface.TeamsListFragment;
import org.openstack.android.summit.modules.teams_notifications_inbox.user_interface.TeamsListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by smarcet on 2/13/17.
 */
@Module
public class TeamsModule {

    @Provides
    public TeamsListFragment providesTeamsListFragment(){
        return new TeamsListFragment();
    }

    @Provides
    public ITeamsListInteractor providesTeamsListInteractor(
            ITeamRemoteDataStore teamRemoteDataStore,
            ISecurityManager securityManager,
            IDTOAssembler dtoAssembler,
            ISummitSelector summitSelector,
            ISummitDataStore summitDataStore
    ){
        return new TeamsListInteractor(teamRemoteDataStore, securityManager, dtoAssembler, summitSelector, summitDataStore);
    }

    @Provides
    public ITeamNotificationsWireframe providesTeamNotificationsWireframe(INavigationParametersStore navigationParametersStore){
        return new TeamNotificationsWireframe(navigationParametersStore);
    }

    @Provides
    public ITeamsListPresenter providesTeamsListPresenter(ITeamsListInteractor interactor, ITeamNotificationsWireframe wireframe){
        return new TeamsListPresenter(interactor, wireframe);
    }
}
