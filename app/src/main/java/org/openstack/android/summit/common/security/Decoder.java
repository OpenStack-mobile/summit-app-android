package org.openstack.android.summit.common.security;

import android.util.Base64;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.common.Constants;

import java.io.UnsupportedEncodingException;

/**
 * Created by Claudio Redi on 4/8/2016.
 */
public class Decoder implements IDecoder {
    @Override
    public String decode(String codedString) throws UnsupportedEncodingException {
        byte[] data = Base64.decode(codedString, Base64.DEFAULT);
        String text = new String(data, "UTF-8");
        return text;
    }

    public String getClientSecretOIDC() {
        String resourceServerUrl = "";
        try {
            if (BuildConfig.DEBUG) {
                resourceServerUrl = decode(Constants.ConfigOIDC.TEST_CLIENT_SECRET);
            } else {
                resourceServerUrl = decode(Constants.ConfigOIDC.PRODUCTION_CLIENT_SECRET);
            }
        } catch (UnsupportedEncodingException e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, "Couldn't decode", e);
        }

        return resourceServerUrl;
    }

    public String getClientIDOIDC() {
        String value = "";
        try {
            if (BuildConfig.DEBUG) {
                value = decode(Constants.ConfigOIDC.TEST_CLIENT_ID);
            }
            else {
                value = decode(Constants.ConfigOIDC.PRODUCTION_CLIENT_ID);
            }
        }
        catch (UnsupportedEncodingException e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, "Couldn't decode", e);
        }
        return value;
    }

    public String getClientSecretServiceAccount() {
        String resourceServerUrl = "";
        try {
            if (BuildConfig.DEBUG) {
                resourceServerUrl = decode(Constants.ConfigServiceAccount.TEST_CLIENT_SECRET);
            } else {
                resourceServerUrl = decode(Constants.ConfigServiceAccount.PRODUCTION_CLIENT_SECRET);
            }
        } catch (UnsupportedEncodingException e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, "Couldn't decode", e);
        }

        return resourceServerUrl;
    }

    public String getClientIDServiceAccount() {
        String value = "";
        try {
            if (BuildConfig.DEBUG) {
                value = decode(Constants.ConfigServiceAccount.TEST_CLIENT_ID);
            }
            else {
                value = decode(Constants.ConfigServiceAccount.PRODUCTION_CLIENT_ID);
            }
        }
        catch (UnsupportedEncodingException e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, "Couldn't decode", e);
        }
        return value;
    }

    public String getTokenServerUrl() {
        String value = "";
        if (BuildConfig.DEBUG) {
            value = Constants.TEST_TOKEN_SERVER_URL;
        }
        else {
            value = Constants.PRODUCTION_TOKEN_SERVER_URL;
        }
        return value;
    }

    public String[] getScopesOIDC() {
        String[] value = null;
        if (BuildConfig.DEBUG) {
            value = Constants.ConfigOIDC.TEST_SCOPES;
        }
        else {
            value = Constants.ConfigOIDC.PRODUCTION_SCOPES;
        }
        return value;
    }


    public String[] getScopesServiceAccount() {
        String[] value = null;
        if (BuildConfig.DEBUG) {
            value = Constants.ConfigServiceAccount.TEST_SCOPES;
        }
        else {
            value = Constants.ConfigServiceAccount.PRODUCTION_SCOPES;
        }
        return value;
    }

    public String getAuthorizationServerUrl() {
        String value = "";
        if (BuildConfig.DEBUG) {
            value = Constants.TEST_AUTHORIZATION_SERVER_URL;
        }
        else {
            value = Constants.PRODUCTION_AUTHORIZATION_SERVER_URL;
        }
        return value;
    }
}
