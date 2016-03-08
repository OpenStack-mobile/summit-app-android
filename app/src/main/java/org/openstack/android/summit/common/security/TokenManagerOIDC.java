package org.openstack.android.summit.common.security;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;

import java.io.IOException;

/**
 * Created by Claudio Redi on 12/8/2015.
 */
public class TokenManagerOIDC implements ITokenManager {
    @Override
    public String getToken() throws TokenGenerationException {
        final AccountManager accountManager = AccountManager.get(OpenStackSummitApplication.context);
        Account account = getOIDCAccount();
        String token = null;
        if (account != null) {
            AccountManagerFuture<Bundle> futureManager = accountManager.getAuthToken(account, Authenticator.TOKEN_TYPE_ACCESS, null, true, null, null);
            try {
                token = futureManager.getResult().getString(AccountManager.KEY_AUTHTOKEN);
            } catch (Exception e) {
                Crashlytics.logException(e);
                Log.e(Constants.LOG_TAG,"Error getting token", e);
                throw new TokenGenerationException(e);
            }
        }
        return token;
    }

    @Override
    public void invalidateToken(String token) {
        final AccountManager accountManager = AccountManager.get(OpenStackSummitApplication.context);
        final String accountType = OpenStackSummitApplication.context.getString(R.string.ACCOUNT_TYPE);
        accountManager.invalidateAuthToken(accountType, token);
    }

    private Account getOIDCAccount() {
        final AccountManager accountManager = AccountManager.get(OpenStackSummitApplication.context);
        final String accountType = OpenStackSummitApplication.context.getString(R.string.ACCOUNT_TYPE);
        Account availableAccounts[] = accountManager.getAccountsByType(accountType);
        Account account = availableAccounts.length > 0 ? availableAccounts[0] : null;
        return account;
    }
}