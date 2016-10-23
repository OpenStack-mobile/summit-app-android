package org.openstack.android.summit.common.security;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;

/**
 * Created by Claudio Redi on 12/8/2015.
 */
public class TokenManagerOIDC implements ITokenManager {
    @Override
    public String getToken() throws TokenGenerationException {
        String token                        = null;
        final AccountManager accountManager = AccountManager.get(OpenStackSummitApplication.context);
        Account account                     = getOIDCAccount();

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
        Account account = null;
        try {
            final AccountManager accountManager = AccountManager.get(OpenStackSummitApplication.context);
            final String accountType            = OpenStackSummitApplication.context.getString(R.string.ACCOUNT_TYPE);
            Account availableAccounts[]         = accountManager.getAccountsByType(accountType);
            account                             = availableAccounts.length > 0 ? availableAccounts[0] : null;

        }
        catch (SecurityException ex1){
            Log.e(Constants.LOG_TAG, ex1.getMessage());
            Crashlytics.logException(ex1);
        }
        catch (Exception ex){
            Log.e(Constants.LOG_TAG, ex.getMessage());
            Crashlytics.logException(ex);
        }
        return account;
    }
}