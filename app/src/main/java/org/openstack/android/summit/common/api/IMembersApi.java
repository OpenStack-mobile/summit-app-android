package org.openstack.android.summit.common.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by smarcet on 11/8/16.
 */

public interface IMembersApi {

    @GET("v1/summits/{summit_id}/members/me")
    Call<ResponseBody> info(@Path("summit_id") int summitId, @Query("expand") String expand);
}
