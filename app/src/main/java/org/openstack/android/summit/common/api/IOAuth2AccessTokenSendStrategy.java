package org.openstack.android.summit.common.api;

import okhttp3.Request;

/**
 * Created by smarcet on 11/9/16.
 */

public interface IOAuth2AccessTokenSendStrategy {

    Request process(Request request, String accessToken);

}
