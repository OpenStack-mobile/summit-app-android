package org.openstack.android.summit.common.security;

/**
 * Created by Claudio Redi on 12/9/2015.
 */
public interface ITokenManagerFactory {
    ITokenManager Create(TokenManagerFactory.TokenManagerType type);
}
