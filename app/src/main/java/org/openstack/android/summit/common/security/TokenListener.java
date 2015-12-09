package org.openstack.android.summit.common.security;

/**
 * Created by Claudio Redi on 12/8/2015.
 */
public interface TokenListener {
    void onSucceed(String token);
    void onError(String token);
}
