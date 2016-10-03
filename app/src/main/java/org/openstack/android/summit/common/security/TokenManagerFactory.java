package org.openstack.android.summit.common.security;

import java.security.InvalidParameterException;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 12/9/2015.
 */
public class TokenManagerFactory implements ITokenManagerFactory {

    TokenManagerOIDC tokenManagerOIDC;
    TokenManagerServiceAccount tokenManagerServiceAccount;

    @Inject
    public TokenManagerFactory(TokenManagerOIDC tokenManagerOIDC, TokenManagerServiceAccount tokenManagerServiceAccount) {
        this.tokenManagerOIDC           = tokenManagerOIDC;
        this.tokenManagerServiceAccount = tokenManagerServiceAccount;
    }

    public enum TokenManagerType {
        OIDC,
        ServiceAccount
    }

    @Override
    public ITokenManager Create(TokenManagerType type) {
        ITokenManager tokenManager;
        switch (type) {
            case OIDC:
                tokenManager = tokenManagerOIDC;
                break;
            case ServiceAccount:
                tokenManager = tokenManagerServiceAccount;
                break;
            default:
                throw new InvalidParameterException(String.format("Type %1 is invalid", type));
        }

        return tokenManager;
    }
}
