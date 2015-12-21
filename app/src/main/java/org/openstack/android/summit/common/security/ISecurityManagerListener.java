package org.openstack.android.summit.common.security;

/**
 * Created by Claudio Redi on 12/16/2015.
 */
public interface ISecurityManagerListener {
    void onLoggedIn();
    void onLoggedOut();
    void onError(String message);
}
