package org.openstack.android.summit.common.security;

import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;

import java.io.IOException;

/**
 * Created by Claudio Redi on 12/8/2015.
 */
public interface ITokenManager {
    String getToken() throws TokenGenerationException;
    void invalidateToken(String token);
}
