package org.openstack.android.summit.common.data_access.repositories.impl.remote;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.api.ITeamsAPI;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.data_access.repositories.remote.ITeamRemoteDataStore;
import org.openstack.android.summit.common.entities.teams.Team;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.List;

import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.exceptions.Exceptions;
import retrofit2.Retrofit;

/**
 * Created by smarcet on 2/13/17.
 */

public class TeamRemoteDataStore implements ITeamRemoteDataStore {

    private Retrofit restClient;
    private ITeamsAPI teamsApi;
    private IDeserializer deserializer;

    public TeamRemoteDataStore(@Named("MemberProfileRXJava2") Retrofit restClient, IDeserializer deserializer){
        this.restClient   = restClient;
        this.teamsApi     = this.restClient.create(ITeamsAPI.class);
        this.deserializer = deserializer;
    }

    @Override
    public Observable<List<Team>> getMyTeams(String expand, int page, int objectsPerPage) {
        return this.teamsApi.getMyTeams(expand, page, objectsPerPage)
               .flatMap(response -> {
                   List<Team> teamList = null;
                   try{
                       teamList = RealmFactory.transaction
                               (session -> deserializer.deserializePage(response.body().string(), Team.class));
                   }
                   catch (Exception ex){
                       Crashlytics.logException(ex);
                       Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
                       throw Exceptions.propagate(ex);
                   }
                   return (teamList !=null) ? Observable.fromArray(teamList): Observable.empty();
               });

    }
}
