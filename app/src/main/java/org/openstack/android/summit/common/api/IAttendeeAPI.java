package org.openstack.android.summit.common.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by smarcet on 2/23/17.
 */

public interface IAttendeeAPI {

    @POST("v1/summits/{summit_id}/attendees/me/schedule/{event_id}")
    Observable<Response<ResponseBody>> addToMySchedule(@Path("summit_id") int summitId, @Path("event_id") Integer eventId);

    @DELETE("v1/summits/{summit_id}/attendees/me/schedule/{event_id}")
    Observable<Response<ResponseBody>> removeFromMySchedule(@Path("summit_id") int summitId, @Path("event_id") Integer eventId);

    @DELETE("v1/summits/{summit_id}/attendees/me/schedule/{event_id}/rsvp")
    Observable<Response<ResponseBody>> deleteRSVP(@Path("summit_id") int summitId, @Path("event_id") Integer eventId);
}
