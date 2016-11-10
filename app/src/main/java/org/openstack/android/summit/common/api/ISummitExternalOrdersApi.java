package org.openstack.android.summit.common.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by smarcet on 11/9/16.
 */

public interface ISummitExternalOrdersApi {

    @GET("v1/summits/{summit_id}/external-orders/{external_order_number}")
    Call<ResponseBody> get(@Path("summit_id") String summitId, @Path("external_order_number") String externalOrderNumber);

    @POST("v1/summits/{summit_id}/external-orders/{external_order_number}/external-attendees/{external_attendee_id}/confirm")
    Call<ResponseBody> confirm(@Path("summit_id") String summitId, @Path("external_order_number") String externalOrderNumber, @Path("external_attendee_id") Integer externalAttendeeId);
}
