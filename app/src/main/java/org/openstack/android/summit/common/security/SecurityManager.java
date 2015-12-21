package org.openstack.android.summit.common.security;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.summit.common.data_access.IMemberDataStore;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.network.HttpTask;
import org.openstack.android.summit.common.network.HttpTaskConfig;
import org.openstack.android.summit.common.network.HttpTaskListener;
import org.openstack.android.summit.common.network.IHttpTaskFactory;

import java.security.spec.InvalidParameterSpecException;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 12/7/2015.
 */
public class SecurityManager implements ISecurityManager {
    private IHttpTaskFactory httpTaskFactory;
    private IMemberDataStore memberDataStore;
    private Member member;
    private ISecurityManagerListener delegate;

    @Inject
    public SecurityManager(IHttpTaskFactory httpTaskFactory, IMemberDataStore memberDataStore) {
        this.httpTaskFactory = httpTaskFactory;
        this.memberDataStore = memberDataStore;
        this.memberDataStore.setDelegate(new IDataStoreOperationListener<Member>() {
            @Override
            public void onSuceedWithData(Member data) {
                member = data;
                if (delegate != null) {
                    delegate.onLoggedIn();
                }
            }

            @Override
            public void onSucceed() {

            }

            @Override
            public void onError(String message) {
                if (delegate != null) {
                    delegate.onError(message);
                }
            }
        });
    }

    @Override
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
            memberDataStore.getLoggedInMemberOrigin();
        }
    }

    @Override
    public void logout() {
        Context context = OpenStackSummitApplication.context;
        final AccountManager accountManager = AccountManager.get(context);
        final String accountType = context.getString(R.string.ACCOUNT_TYPE);
        Account availableAccounts[] = accountManager.getAccountsByType(accountType);

        if (availableAccounts.length == 0) {
            String token = accountManager.peekAuthToken(availableAccounts[0], Authenticator.TOKEN_TYPE_REFRESH);
            if (TextUtils.isEmpty(token)) {
                accountManager.invalidateAuthToken(accountType, token);
            }
        }
        member = null;
        if (delegate != null) {
            delegate.onLoggedOut();
        }
    }

    public ISecurityManagerListener getDelegate() {
        return delegate;
    }

    public void setDelegate(ISecurityManagerListener delegate) {
        this.delegate = delegate;
    }

    public Boolean isLoggedIn() {
        return member != null;
    }
}
