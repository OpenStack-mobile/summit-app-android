package org.openstack.android.summit.common.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by smarcet on 11/9/16.
 */

public interface ISummitEntityEventsApi {

    @GET("v1/summits/{summit_id}/entity-events")
    public Call<ResponseBody> get
    (
        @Path("summit_id") int summitId,
        @Query("from_date") Long fromDateEpoch,
        @Query("last_event_id") Long fromEventId,
        @Query("limit") Integer limit
    );
}
