package org.openstack.android.summit.common.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by smarcet on 2/13/17.
 */

public interface ITeamsAPI
{
    @GET("/api/v1/teams")
    Observable<Response<ResponseBody>> getMyTeams(@Query("expand") String expand,
                                                  @Query("page") Integer page,
                                                  @Query("per_page") Integer perPage);


}
