package org.openstack.android.summit.common.security;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;

/**
 * Created by sebastian on 7/18/2016.
 */
final public class OIDCConfigurationManager extends ConfigurationParamsManager implements IOIDCConfigurationManager {

    private IDecoder decoder;

    private final String TAG = getClass().getSimpleName();

    private static final String NativeClientIdKey          = "org.openstack.android.summit.common.security.NATIVE_CLIENT_ID";
    private static final String NativeClientSecretKey      = "org.openstack.android.summit.common.security.NATIVE_CLIENT_SECRET";
    private static final String NativeClientReturnUrlKey   = "org.openstack.android.summit.common.security.NATIVE_CLIENT_RETURN_URL";

    private static final String ServiceClientIdKey         = "org.openstack.android.summit.common.security.SERVICE_CLIENT_ID";
    private static final String ServiceClientSecretKey     = "org.openstack.android.summit.common.security.SERVICE_CLIENT_SECRET";

    private static final String ResourceServerBaseUrlKey   = "org.openstack.android.summit.common.security.RS_BASE_URL";
    private static final String IdentityProviderBaseUrlKey = "org.openstack.android.summit.common.security.IDP_BASE_URL";

    public OIDCConfigurationManager(IDecoder decoder, IConfigurationParamFinderStrategy[] finderStrategies){
        super(finderStrategies);
        this.decoder          = decoder;
    }

    @Override
    public OIDCClientConfiguration buildConfiguration(OIDCClientConfiguration.ODICAccountType accountType) {
        switch (accountType){
            case NativeAccount:{
                return new OIDCNativeClientConfiguration
                (
                    findConfigParamBy(NativeClientIdKey, true),
                    findConfigParamBy(NativeClientSecretKey, true),
                    findConfigParamBy(NativeClientReturnUrlKey, false),
                    this.buildScopeDefinitionFor(accountType)
                );
            }
            case ServiceAccount:{
                return new OIDCServiceClientConfiguration
                (
                    findConfigParamBy(ServiceClientIdKey, true),
                    findConfigParamBy(ServiceClientSecretKey, true),
                    this.buildScopeDefinitionFor(accountType)
                );
            }
        }
        return null;
    }

    @Override
    public String getResourceServerBaseUrl() {
        return this.findConfigParamBy(ResourceServerBaseUrlKey, false);
    }

    @Override
    public IdentityProviderUrls buildIdentityProviderUrls() {
        return new IdentityProviderUrls(this.findConfigParamBy(IdentityProviderBaseUrlKey, false));
    }

    private String[] buildScopeDefinitionFor(OIDCClientConfiguration.ODICAccountType accountType){
        String rsBaseUrl = this.getResourceServerBaseUrl();
        switch (accountType){
            case NativeAccount:{
                return new String[] {
                    "openid",
                    "offline_access",
                    String.format("%s/summits/read", rsBaseUrl),
                    String.format("%s/summits/write", rsBaseUrl),
                    String.format("%s/summits/read-external-orders", rsBaseUrl),
                    String.format("%s/summits/confirm-external-orders", rsBaseUrl),
                    String.format("%s/me/read", rsBaseUrl),
                    String.format("%s/teams/read", rsBaseUrl),
                    String.format("%s/teams/write", rsBaseUrl),
                    String.format("%s/members/read", rsBaseUrl),
                    String.format("%s/members/invitations/read", rsBaseUrl),
                    String.format("%s/members/invitations/write", rsBaseUrl),
                };
            }
            case ServiceAccount:{
                return new String[] {
                    String.format("%s/summits/read", rsBaseUrl),
                };
            }
        }
        return null;
    }

    public String findConfigParamBy(String key, boolean shallDecode) throws InvalidParameterException {
        try {
            String value= this.findConfigParamBy(key);
            if (value != null) {
                return (shallDecode) ? this.decoder.decode(value) : value;
            }
        }
        catch (UnsupportedEncodingException e){
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        }
        return null;
    }
}