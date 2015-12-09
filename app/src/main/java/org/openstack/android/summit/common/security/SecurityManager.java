package org.openstack.android.summit.common.security;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.os.Bundle;

import com.github.kevinsawicki.http.HttpRequest;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.network.HttpTask;
import org.openstack.android.summit.common.network.HttpTaskConfig;
import org.openstack.android.summit.common.network.HttpTaskListener;

/**
 * Created by Claudio Redi on 12/7/2015.
 */
public class SecurityManager {
    public void login(final Activity context) {
        final AccountManager accountManager = AccountManager.get(context);
        final String accountType = context.getString(R.string.ACCOUNT_TYPE);

        if (accountManager.getAccountsByType(accountType).length == 0) {
            // No account has been created, let's create one now
            accountManager.addAccount(accountType, Authenticator.TOKEN_TYPE_ID, null, null,
                    context, new AccountManagerCallback<Bundle>() {
                        @Override
                        public void run(AccountManagerFuture<Bundle> futureManager) {
                            // Unless the account creation was cancelled, try logging in again
                            // after the account has been created.
                            if (futureManager.isCancelled()) return;
                            login(context);
                        }
                    }, null);
        }
        else {
            HttpTaskConfig httpTaskConfig = new HttpTaskConfig();
            httpTaskConfig.setTokenManager(new TokenManagerOIDC());
            httpTaskConfig.setContext(context);
            httpTaskConfig.setMethod(HttpRequest.METHOD_GET);
            httpTaskConfig.setUrl("https://testresource-server.openstack.org/api/v1/summits/current/attendees/me?expand=speaker,feedback");
            httpTaskConfig.setDelegate(new HttpTaskListener() {
                @Override
                public void onSucceed(String data) {

                }

                @Override
                public void onError(String error) {

                }
            });
            new HttpTask().execute(httpTaskConfig);
        }
    }
}
