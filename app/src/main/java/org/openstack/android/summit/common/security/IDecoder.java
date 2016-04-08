package org.openstack.android.summit.common.security;

import java.io.UnsupportedEncodingException;

/**
 * Created by Claudio Redi on 4/8/2016.
 */
public interface IDecoder {
    String decode(String codedString) throws UnsupportedEncodingException;
    String getClientSecretOIDC();
    String getClientIDOIDC();
    String getClientSecretServiceAccount();
    String getClientIDServiceAccount();
    String getTokenServerUrl();
    String[] getScopesOIDC();
    String[] getScopesServiceAccount();
    String getAuthorizationServerUrl();
}

