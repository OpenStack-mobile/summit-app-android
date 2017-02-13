package org.openstack.android.summit.modules.teams_notifications_inbox.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.TeamDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.remote.ITeamRemoteDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by smarcet on 2/13/17.
 */

public class TeamsListInteractor extends BaseInteractor implements ITeamsListInteractor {

    private ITeamRemoteDataStore teamRemoteDataStore;

    public TeamsListInteractor
            (
                    ITeamRemoteDataStore teamRemoteDataStore,
                    ISecurityManager securityManager,
                    IDTOAssembler dtoAssembler,
                    ISummitSelector summitSelector,
                    ISummitDataStore summitDataStore) {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore);

        this.teamRemoteDataStore = teamRemoteDataStore;
    }

    @Override
    public Observable<List<TeamDTO>> loadMyTeams(){
        return this.teamRemoteDataStore.getMyTeams("member,owner,groups", 1, 100)
                .flatMap(l -> Observable.fromArray(createDTOList(l, TeamDTO.class)));
    }
}
