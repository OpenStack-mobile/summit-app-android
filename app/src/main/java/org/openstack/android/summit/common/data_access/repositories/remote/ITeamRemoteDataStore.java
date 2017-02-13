package org.openstack.android.summit.common.data_access.repositories.remote;

import org.openstack.android.summit.common.entities.teams.Team;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by smarcet on 2/13/17.
 */

public interface ITeamRemoteDataStore extends IBaseRemoteDataStore {

    public Observable<List<Team>> getMyTeams(String expand, int page, int objectsPerPage);
}
