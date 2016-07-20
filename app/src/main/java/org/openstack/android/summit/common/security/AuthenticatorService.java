package org.openstack.android.summit.common.security;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.openstack.android.summit.OpenStackSummitApplication;

import javax.inject.Inject;

/**
 * The service that lets Android know about the custom Authenticator.
 *
 * @author Leo Nikkilä
 */
public class AuthenticatorService extends Service {

    private final String TAG = getClass().getSimpleName();
    @Inject
    IOIDCConfigurationManager oidcConfigurationManager;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Binding Authenticator.");
        ((OpenStackSummitApplication)getApplication()).getApplicationComponent().inject(this);
        Authenticator authenticator = new Authenticator(this, this.oidcConfigurationManager);
        return authenticator.getIBinder();
    }
}
