package org.openstack.android.summit.common.security;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.summit.common.data_access.IMemberDataStore;
import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.network.AuthorizationException;
import org.openstack.android.summit.common.network.HttpTaskResult;
import org.openstack.android.summit.common.network.IHttpTaskFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 12/7/2015.
 */
public class SecurityManager implements ISecurityManager {
    private IMemberDataStore memberDataStore;
    private Member member;
    private ISecurityManagerListener delegate;
    private ISession session;
    private ITokenManager tokenManager;

    @Inject
    public SecurityManager(ITokenManager tokenManager, final IMemberDataStore memberDataStore, final ISession session) {
        this.memberDataStore = memberDataStore;
        this.session = session;
        this.tokenManager = tokenManager;
    }

    private void checkForIllegalState() {
        AsyncTask<Void, Void, String> checkTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String token = null;
                try {
                    token = tokenManager.getToken();
                } catch (TokenGenerationException e) {
                    Crashlytics.logException(e);
                    Log.e(Constants.LOG_TAG,e.getMessage(), e);
                }
                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                final AccountManager accountManager = AccountManager.get(OpenStackSummitApplication.context);
                final String accountType = OpenStackSummitApplication.context.getString(R.string.ACCOUNT_TYPE);

                int currentMemberId = session.getInt(Constants.CURRENT_MEMBER_ID);
                Account[] accounts = accountManager.getAccountsByType(accountType);
                if (accounts.length > 0) {
                    if ((token != null && currentMemberId == 0) ||
                            (token == null && currentMemberId != 0)) {
                        logout(false);
                    }
                }
            }
        };
        checkTask.execute();
    }

    public void init() {
        checkForIllegalState();
    }

    @Override
    public void handleIllegalState() {
        if (isLoggedIn()){
            logout();
        }
    }

    @Override
    public void login(final Activity context) {
        final AccountManager accountManager = AccountManager.get(OpenStackSummitApplication.context);
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
        } else {

            IDataStoreOperationListener<Member> dataStoreOperationListener = new DataStoreOperationListener<Member>() {
                @Override
                public void onSuceedWithSingleData(Member data) {
                    member = data;
                    session.setInt(Constants.CURRENT_MEMBER_ID, member.getId());

                    Intent intent = new Intent(Constants.LOGGED_IN_EVENT);
                    LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);

                    if (delegate != null) {
                        delegate.onLoggedIn();
                    }
                }

                @Override
                public void onError(String message) {
                    String userFriendlyError = !message.startsWith("404") ? message : context.getResources().getString(R.string.not_summit_attendee);
                    if (delegate != null) {
                        delegate.onError(userFriendlyError);
                    }
                }
            };
            memberDataStore.getLoggedInMemberOrigin(dataStoreOperationListener);
        }
    }

    @Override
    public void logout() {
        logout(true);
    }

    private void logout(boolean sendNotification) {
        Context context = OpenStackSummitApplication.context;
        final AccountManager accountManager = AccountManager.get(context);
        final String accountType = context.getString(R.string.ACCOUNT_TYPE);
        Account availableAccounts[] = accountManager.getAccountsByType(accountType);

        if (availableAccounts.length > 0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                accountManager.removeAccountExplicitly(availableAccounts[0]);
            } else {
                accountManager.removeAccount(availableAccounts[0], null, null);
            }
        }
        member = null;
        session.setInt(Constants.CURRENT_MEMBER_ID, 0);

        if (sendNotification) {
            Intent intent = new Intent(Constants.LOGGED_OUT_EVENT);
            LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);
        }

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

    @Override
    public Member getCurrentMember() {
        // TODO: check if token is valid
        int currentMemberId = session.getInt(Constants.CURRENT_MEMBER_ID);
        if (currentMemberId > 0) {
            member = memberDataStore.getByIdLocal(currentMemberId);
        }
        return member;
    }

    @Override
    public Boolean isLoggedIn() {
        final AccountManager accountManager = AccountManager.get(OpenStackSummitApplication.context);
        final String accountType = OpenStackSummitApplication.context.getString(R.string.ACCOUNT_TYPE);

        int currentMemberId = session.getInt(Constants.CURRENT_MEMBER_ID);

        if (currentMemberId > 0) {
            member = memberDataStore.getByIdLocal(currentMemberId);
        }

        return accountManager.getAccountsByType(accountType).length > 0 && currentMemberId > 0 && member != null;
    }

    public List<MemberRole> getLoggedInMemberRoles() {
        if (!isLoggedIn()) {
            return null;
        }

        List<MemberRole> roles = new ArrayList<>();
        Member member = getCurrentMember();
        if (member.getAttendeeRole() != null) {
            roles.add(MemberRole.Attendee);
        }
        if (member.getSpeakerRole() != null) {
            roles.add(MemberRole.Speaker);
        }

        return roles;
    }
}
