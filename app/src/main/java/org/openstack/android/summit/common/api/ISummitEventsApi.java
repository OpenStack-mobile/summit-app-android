package org.openstack.android.summit.common.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by smarcet on 11/8/16.
 */

public interface ISummitEventsApi {

    @GET("v1/summits/{summit_id}/events/{event_id}/published")
    public Call<ResponseBody> getPublishedEvent(@Path("summit_id") int summitId, @Path("event_id") Integer eventId, @Query("fields") String fields, @Query("relations") String relations);

    @GET("v1/summits/{summit_id}/events/{event_id}/feedback")
    public Call<ResponseBody> getEventFeedback
    (
        @Path("summit_id") int summitId,
        @Path("event_id") Integer eventId,
        @Query("expand") String expand,
        @Query("page") Integer page,
        @Query("per_page") Integer perPage
    );

    @POST("v2/summits/{summit_id}/events/{event_id}/feedback")
    public Call<ResponseBody> postEventFeedback(@Path("summit_id") int summitId, @Path("event_id") Integer eventId, @Body SummitEventFeedbackRequest body);
}
