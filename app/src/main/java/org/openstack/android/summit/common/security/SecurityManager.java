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
import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.summit.common.data_access.IMemberRemoteDataStore;
import org.openstack.android.summit.common.data_access.repositories.IMemberDataStore;
import org.openstack.android.summit.common.data_access.deserialization.DataStoreOperationListener;
import org.openstack.android.summit.common.data_access.repositories.impl.MemberDataStore;
import org.openstack.android.summit.common.entities.Member;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Claudio Redi on 12/7/2015.
 */
public class SecurityManager implements ISecurityManager {

    enum SecurityManagerState {
        IDLE,
        LOGGED_OUT,
        ON_LOGIN_PROCESS,
        LOGGED_IN,
    }

    private IMemberDataStore memberDataStore;
    private IPrincipalIdentity identity;
    private ITokenManager    tokenManager;

    // internal state
    private SecurityManagerState state  = SecurityManagerState.IDLE;

    @Inject
    public SecurityManager
    (
        ITokenManager tokenManager,
        IMemberDataStore memberDataStore,
        IPrincipalIdentity identity
    )
    {
        this.memberDataStore = memberDataStore;
        this.identity        = identity;
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
                    if(state == SecurityManagerState.ON_LOGIN_PROCESS) return;

                    final AccountManager accountManager = AccountManager.get(OpenStackSummitApplication.context);
                    final String accountType            = OpenStackSummitApplication.context.getString(R.string.ACCOUNT_TYPE);
                    int currentMemberId                 = identity.getCurrentMemberId();
                    Account[] accounts                  = accountManager.getAccountsByType(accountType);

                    if (accounts.length > 0) {
                        if ((token != null && currentMemberId == 0) || (token == null && currentMemberId != 0)) {
                            logout(true);
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
            logout(true);
        }
    }

    @Override
    public void login(final Activity context) {
        try{
            state = SecurityManagerState.ON_LOGIN_PROCESS;

            Log.d(Constants.LOG_TAG, "SecurityManager.login");

            final AccountManager accountManager = AccountManager.get(OpenStackSummitApplication.context);
            final String accountType            = context.getString(R.string.ACCOUNT_TYPE);
            Intent intent                       = new Intent(Constants.START_LOG_IN_EVENT);
            Account availableAccounts[]         = accountManager.getAccountsByType(accountType);

            if(availableAccounts.length > 0){
                removeAccount(availableAccounts[0]);
            }

            LocalBroadcastManager
                    .getInstance(OpenStackSummitApplication.context)
                    .sendBroadcast(intent);

            accountManager.addAccount
            (
                accountType,
                Authenticator.TOKEN_TYPE_ID,
                null,
                null,
                context,
                    futureManager -> {
                        // Unless the account creation was cancelled, try logging in again
                        // after the account has been created.
                        if (futureManager.isCancelled()) return;
                        bindCurrentUser();
                    },
                null
            );
        }
        catch(SecurityException ex1){
            Log.w(Constants.LOG_TAG, ex1.getMessage());
            Crashlytics.logException(ex1);
            state = SecurityManagerState.IDLE;
        }
        catch(Exception ex){
            Log.e(Constants.LOG_TAG, ex.getMessage());
            Crashlytics.logException(ex);
            state = SecurityManagerState.IDLE;
        }
    }

    @Override
    public void bindCurrentUser(){
        Log.d(Constants.LOG_TAG, "SecurityManager.bindCurrentUser");

        memberDataStore.getLoggedInMember()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe
                (
                    memberId -> {
                        identity.setCurrentMemberId(memberId);
                        Intent intent = new Intent(Constants.LOGGED_IN_EVENT);
                        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);
                        state = SecurityManagerState.LOGGED_IN;
                    },
                    (ex) -> {
                        Log.e(Constants.LOG_TAG, ex.toString());
                        Intent intent = new Intent(Constants.LOG_IN_ERROR_EVENT);
                        intent.putExtra(Constants.LOG_IN_ERROR_MESSAGE, ex.getMessage());
                        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);
                        state = SecurityManagerState.IDLE;
                    }
                );
    }

    private void removeAccount(Account account){
        final AccountManager accountManager = AccountManager.get(OpenStackSummitApplication.context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            accountManager.removeAccountExplicitly(account);
            return;
        }
        accountManager.removeAccount(account, null, null);
    }

    @Override
    public void logout(boolean enabledDataUpdates) {

        try {
            Context context                     = OpenStackSummitApplication.context;
            final AccountManager accountManager = AccountManager.get(context);
            final String accountType            = context.getString(R.string.ACCOUNT_TYPE);
            Account availableAccounts[]         = accountManager.getAccountsByType(accountType);

            if (availableAccounts.length > 0) {
                removeAccount(availableAccounts[0]);
            }

            identity.clearCurrentMember();

            Intent intent = new Intent(Constants.LOGGED_OUT_EVENT);
            intent.putExtra(Constants.EXTRA_ENABLE_DATA_UPDATES_AFTER_LOGOUT, enabledDataUpdates);
            LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);

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

            if(identity.getCurrentMemberId() > 0){
                return memberDataStore.getById(identity.getCurrentMemberId());
            }
            return null;
        }
        catch(SecurityException ex1){
            Log.w(Constants.LOG_TAG, ex1.getMessage());
            Crashlytics.logException(ex1);
        }
        catch(Exception ex){
            Log.e(Constants.LOG_TAG, ex.getMessage());
            Crashlytics.logException(ex);
        }
        return null;
    }

    @Override
    public boolean isLoggedIn() {
        return getCurrentMember() != null;
    }

    @Override
    public boolean isLoggedInAndConfirmedAttendee() {
        Member member = getCurrentMember();
        return member != null && member.getAttendeeRole() != null;
    }
}
