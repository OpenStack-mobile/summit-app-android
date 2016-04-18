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

import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.summit.common.data_access.IMemberDataStore;
import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.network.AuthorizationException;
import org.openstack.android.summit.common.network.HttpTask;
import org.openstack.android.summit.common.network.HttpTaskListener;
import org.openstack.android.summit.common.network.HttpTaskResult;
import org.openstack.android.summit.common.network.IHttpTaskFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.fabric.sdk.android.services.network.HttpMethod;

/**
 * Created by Claudio Redi on 12/7/2015.
 */
public class SecurityManager implements ISecurityManager {
    private IMemberDataStore memberDataStore;
    private Member member;
    private ISecurityManagerListener delegate;
    private ISession session;
    private ITokenManager tokenManager;
    private final int LOGGED_IN_NOT_CONFIRMED_ATTENDEE_ID = -1;
    private final String HACK_FIX_MEMBER_ID = "HACK_FIX_MEMBER_ID";
    private boolean hackForFixWrongMemberIDDoneOrInProgress;

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
                    if ((token != null && currentMemberId == 0) || (token == null && currentMemberId != 0)) {
                        logout(false);
                    }
                }
            }
        };
        checkTask.execute();
    }

    public void init() {
        int currentMemberId = session.getInt(Constants.CURRENT_MEMBER_ID);
        if (currentMemberId == LOGGED_IN_NOT_CONFIRMED_ATTENDEE_ID && member == null) {
            member = new Member();
            member.setId(currentMemberId);
            member.setFullName(session.getString(Constants.CURRENT_MEMBER_NAME));
        }
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
            linkAttendeeIfExist();
        }
    }

    public void linkAttendeeIfExist() {
        final IDataStoreOperationListener<Member> dataStoreOperationListenerNonRegisteredAttendee = new DataStoreOperationListener<Member>() {
            @Override
            public void onSuceedWithSingleData(Member data) {
                member = data;
                if (data != null && data.getId() > 0) {
                    session.setInt(Constants.CURRENT_MEMBER_ID, member.getId());
                }
                else {
                    session.setInt(Constants.CURRENT_MEMBER_ID, LOGGED_IN_NOT_CONFIRMED_ATTENDEE_ID);
                }
                session.setString(Constants.CURRENT_MEMBER_NAME, member.getFullName());

                Intent intent = new Intent(Constants.LOGGED_IN_EVENT);
                LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);

                if (delegate != null) {
                    delegate.onLoggedIn();
                }
            }

            @Override
            public void onError(String message) {
                if (delegate != null) {
                    delegate.onError(message);
                }
            }
        };

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
                if (message.startsWith("404")) {
                    memberDataStore.getLoggedInMemberBasicInfoOrigin(dataStoreOperationListenerNonRegisteredAttendee);
                    return;
                }

                if (delegate != null) {
                    delegate.onError(message);
                }
            }
        };

        memberDataStore.getLoggedInMemberOrigin(dataStoreOperationListener);
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
        session.setString(Constants.CURRENT_MEMBER_NAME, null);

        if (sendNotification) {
            Intent intent = new Intent(Constants.LOGGED_OUT_EVENT);
            LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);

            if (delegate != null) {
                delegate.onLoggedOut();
            }
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

            // Chili #10927. Delete this after summit
            hackForFixWrongMemberID();
        }

        return accountManager.getAccountsByType(accountType).length > 0 && currentMemberId != 0 && member != null;
    }

    private void hackForFixWrongMemberID() {
        if (!hackForFixWrongMemberIDDoneOrInProgress) {
            String flag = session.getString(HACK_FIX_MEMBER_ID);
            if (flag == null) {
                hackForFixWrongMemberIDDoneOrInProgress = true;
                Log.d(Constants.LOG_TAG, "Hack to fix wrong member id in progress");

                IDataStoreOperationListener<Member> dataStoreOperationListener = new DataStoreOperationListener<Member>() {
                    @Override
                    public void onSuceedWithSingleData(Member data) {
                        session.setString(HACK_FIX_MEMBER_ID, "DONE");
                        session.setInt(Constants.CURRENT_MEMBER_ID, data.getId());
                    }
                };

                memberDataStore.getLoggedInMemberOrigin(dataStoreOperationListener);
            }
        }
    }

    @Override
    public boolean isLoggedInAndConfirmedAttendee() {
        int currentMemberId = session.getInt(Constants.CURRENT_MEMBER_ID);
        return isLoggedIn() && currentMemberId != LOGGED_IN_NOT_CONFIRMED_ATTENDEE_ID;
    }
}
