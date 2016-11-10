package org.openstack.android.summit.common.api;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created by smarcet on 11/9/16.
 */

final public class OAuth2AccessTokenPostSendStrategy implements IOAuth2AccessTokenSendStrategy {

    public Request process(Request request, String accessToken){

        HttpUrl originalHttpUrl = request.url();

        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter("access_token", accessToken)
                .build();

        Request.Builder requestBuilder = request.newBuilder()
                .url(url)
                .method(request.method(), request.body());

        return requestBuilder.build();
    }
}
