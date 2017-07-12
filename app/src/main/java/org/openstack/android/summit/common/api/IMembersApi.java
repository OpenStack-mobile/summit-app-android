package org.openstack.android.summit.common.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by smarcet on 11/8/16.
 */

public interface IMembersApi {

    @GET("v1/summits/{summit_id}/members/me")
    Observable<Response<ResponseBody>> info(@Path("summit_id") int summitId, @Query("expand") String expand);

    @POST("v1/summits/{summit_id}/members/me/favorites/{event_id}")
    Observable<Response<ResponseBody>> addToFavorites(@Path("summit_id") int summitId, @Path("event_id") int eventId);

    @DELETE("v1/summits/{summit_id}/members/me/favorites/{event_id}")
    Observable<Response<ResponseBody>> removeFromFavorites(@Path("summit_id") int summitId, @Path("event_id") int eventId);

    @POST("v1/summits/{summit_id}/members/me/schedule/{event_id}")
    Observable<Response<ResponseBody>> addToMySchedule(@Path("summit_id") int summitId, @Path("event_id") Integer eventId);

    @DELETE("v1/summits/{summit_id}/members/me/schedule/{event_id}")
    Observable<Response<ResponseBody>> removeFromMySchedule(@Path("summit_id") int summitId, @Path("event_id") Integer eventId);

    @DELETE("v1/summits/{summit_id}/members/me/schedule/{event_id}/rsvp")
    Observable<Response<ResponseBody>> deleteRSVP(@Path("summit_id") int summitId, @Path("event_id") Integer eventId);
}
