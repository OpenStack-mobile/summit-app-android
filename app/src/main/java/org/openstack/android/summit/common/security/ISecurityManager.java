package org.openstack.android.summit.common.security;

import android.app.Activity;

/**
 * Created by Claudio Redi on 12/15/2015.
 */
public interface ISecurityManager {
    void login(Activity context);
    void logout();
    Boolean isLoggedIn();
    ISecurityManagerListener getDelegate();
    void setDelegate(ISecurityManagerListener delegate);
}
