package org.openstack.android.summit.common.security;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;

import java.io.IOException;

/**
 * Created by Claudio Redi on 12/8/2015.
 */
public class TokenManagerOIDC implements ITokenManager {
    @Override
    public String getToken() throws AuthenticatorException, OperationCanceledException, IOException {
        final AccountManager accountManager = AccountManager.get(OpenStackSummitApplication.context);
        Account account = getOIDCAccount();
        AccountManagerFuture<Bundle> futureManager = accountManager.getAuthToken(account, Authenticator.TOKEN_TYPE_ACCESS, null, true, null, null);
        String token = futureManager.getResult().getString(AccountManager.KEY_AUTHTOKEN);
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
        Account account = availableAccounts[0];
        return account;
    }
}
