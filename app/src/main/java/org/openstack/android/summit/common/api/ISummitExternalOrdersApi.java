package org.openstack.android.summit.common.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by smarcet on 11/9/16.
 */

public interface ISummitExternalOrdersApi {

    @GET("v1/summits/{summit_id}/external-orders/{external_order_number}")
    Observable<Response<ResponseBody>> get(@Path("summit_id") int summitId, @Path("external_order_number") String externalOrderNumber);

    @POST("v1/summits/{summit_id}/external-orders/{external_order_number}/external-attendees/{external_attendee_id}/confirm")
    Observable<Response<ResponseBody>> confirm(@Path("summit_id") int summitId, @Path("external_order_number") String externalOrderNumber, @Path("external_attendee_id") Integer externalAttendeeId);
}
