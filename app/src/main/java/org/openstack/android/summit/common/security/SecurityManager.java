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

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 12/7/2015.
 */
public class SecurityManager implements ISecurityManager {

    private IMemberDataStore memberDataStore;
    private Member member;
    private ISession session;
    private ITokenManager tokenManager;

    @Inject
    public SecurityManager(ITokenManager tokenManager, final IMemberDataStore memberDataStore, final ISession session) {
        this.memberDataStore = memberDataStore;
        this.session         = session;
        this.tokenManager    = tokenManager;
    }

    /**
     * check for token valid states
     */
    private void checkTokenValidState() {

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
                try{
                    final AccountManager accountManager = AccountManager.get(OpenStackSummitApplication.context);
                    final String accountType            = OpenStackSummitApplication.context.getString(R.string.ACCOUNT_TYPE);
                    int currentMemberId                 = session.getInt(Constants.CURRENT_MEMBER_ID);
                    Account[] accounts                  = accountManager.getAccountsByType(accountType);

                    if (accounts.length > 0) {
                        if ((token != null && currentMemberId == 0) || (token == null && currentMemberId != 0)) {
                            logout(false);
                        }
                    }
                }
                catch(SecurityException ex1){
                    Log.w(Constants.LOG_TAG, ex1.getMessage());
                    Crashlytics.logException(ex1);
                }
                catch(Exception ex){
                    Log.e(Constants.LOG_TAG, ex.getMessage());
                    Crashlytics.logException(ex);
                }
            }
        };
        checkTask.execute();
    }

    public void init() {
        checkTokenValidState();
    }

    @Override
    public void handleIllegalState() {
        if (isLoggedIn()){
            logout();
        }
    }

    @Override
    public void login(final Activity context) {
        try{
            final AccountManager accountManager = AccountManager.get(OpenStackSummitApplication.context);
            final String accountType            = context.getString(R.string.ACCOUNT_TYPE);
            Log.d(Constants.LOG_TAG, "SecurityManager.login");
            Intent intent = new Intent(Constants.START_LOG_IN_EVENT);
            LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);
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
                return;
            }
            bindCurrentUser();
        }
        catch(SecurityException ex1){
            Log.w(Constants.LOG_TAG, ex1.getMessage());
            Crashlytics.logException(ex1);
        }
        catch(Exception ex){
            Log.e(Constants.LOG_TAG, ex.getMessage());
            Crashlytics.logException(ex);
        }
    }

    @Override
    public void bindCurrentUser(){
        Log.d(Constants.LOG_TAG, "SecurityManager.bindCurrentUser");
        IDataStoreOperationListener<Member> dataStoreOperationListener = new DataStoreOperationListener<Member>() {
            @Override
            public void onSucceedWithSingleData(Member data) {
                Log.d(Constants.LOG_TAG, "SecurityManager.onSucceedWithSingleData");
                member = data;
                session.setInt(Constants.CURRENT_MEMBER_ID, member.getId());

                Intent intent = new Intent(Constants.LOGGED_IN_EVENT);
                LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);
            }

            @Override
            public void onError(String message) {

                Intent intent = new Intent(Constants.LOG_IN_ERROR_EVENT);
                intent.putExtra(Constants.LOG_IN_ERROR_MESSAGE, message);
                LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);
            }
        };
        memberDataStore.getLoggedInMemberOrigin(dataStoreOperationListener);
    }

    @Override
    public void logout() {
        logout(true);
    }

    private void logout(boolean sendNotification) {

        try {
            Context context                     = OpenStackSummitApplication.context;
            final AccountManager accountManager = AccountManager.get(context);
            final String accountType            = context.getString(R.string.ACCOUNT_TYPE);
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
        }
        catch(SecurityException ex1){
            Log.w(Constants.LOG_TAG, ex1.getMessage());
            Crashlytics.logException(ex1);
        }
        catch(Exception ex){
            Log.e(Constants.LOG_TAG, ex.getMessage());
            Crashlytics.logException(ex);
        }
    }

    @Override
    public Member getCurrentMember() {
        try{
            final AccountManager accountManager = AccountManager.get(OpenStackSummitApplication.context);
            final String accountType            = OpenStackSummitApplication.context.getString(R.string.ACCOUNT_TYPE);

            if(accountManager.getAccountsByType(accountType).length == 0) return null;

            int currentMemberId = session.getInt(Constants.CURRENT_MEMBER_ID);

            if(currentMemberId == 0) return null;

            member = memberDataStore.getByIdLocal(currentMemberId);
        }
        catch(SecurityException ex1){
            Log.w(Constants.LOG_TAG, ex1.getMessage());
            Crashlytics.logException(ex1);
        }
        catch(Exception ex){
            Log.e(Constants.LOG_TAG, ex.getMessage());
            Crashlytics.logException(ex);
        }
        return member;
    }

    @Override
    public boolean isLoggedIn() {
        return getCurrentMember() != null;
    }

    @Override
    public boolean isLoggedInAndConfirmedAttendee() {
        return isLoggedIn() && member.getAttendeeRole() != null;
    }
}
