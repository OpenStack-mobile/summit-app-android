package org.openstack.android.summit.common.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface INotificationsApi {

    @GET("v1/summits/{summit_id}/notifications/sent")
    Observable<Response<ResponseBody>> getSent(
            @Path("summit_id") int summitId,
            @Query("page") int page,
            @Query("per_page") int per_page
    );

}
