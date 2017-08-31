package org.openstack.android.summit.common.security;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.crashlytics.android.Crashlytics;
import com.google.api.client.auth.openidconnect.IdTokenResponse;
import com.google.api.client.json.gson.GsonFactory;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.security.oidc.AuthCodeResponse;
import org.openstack.android.summit.common.security.oidc.IOIDCConfigurationManager;
import org.openstack.android.summit.common.security.oidc.OIDCClientConfiguration;
import org.openstack.android.summit.common.security.oidc.OIDCNativeClientConfiguration;
import org.openstack.android.summit.common.security.oidc.OpenIdConnectProtocol;
import org.openstack.android.summit.common.user_interface.AlertsBuilder;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.Set;

import javax.inject.Inject;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;

/**
 * An Activity that is launched by the Authenticator for requesting authorisation from the user and
 * creating an Account.
 * <p/>
 * The user will interact with the OIDC server via a WebView that monitors the URL for parameters
 * that indicate either a successful authorisation or an error. These parameters are set by the
 * spec.
 * <p/>
 * After the Authorization Token has successfully been obtained, we use the single-use token to
 * fetch an ID Token, an Access Token and a Refresh Token. We create an Account and persist these
 * tokens.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    public static final String KEY_AUTH_URL       = "org.openstack.android.summit.KEY_AUTH_URL";
    public static final String KEY_IS_NEW_ACCOUNT = "org.openstack.android.summit.KEY_IS_NEW_ACCOUNT";
    public static final String KEY_ACCOUNT_OBJECT = "org.openstack.android.summit.KEY_ACCOUNT_OBJECT";
    public static final String LAST_AUTH_CODE     = "org.openstack.android.summit.LAST_AUTH_CODE";

    private AccountManager accountManager;
    private Account account;
    private boolean isNewAccount;
    private boolean workflowCompleted = false;

    private ACProgressPie progressDialog;

    public void setWorkflowCompleted() {
        workflowCompleted = true;
    }

    @Inject
    IOIDCConfigurationManager oidcConfigurationManager;

    @Inject
    ISession session;

    public void showActivityIndicator() {
        try {
            if (progressDialog != null) return;
            progressDialog = new ACProgressPie.Builder(this)
                    .ringColor(Color.WHITE)
                    .pieColor(Color.WHITE)
                    .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                    .build();
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        catch (Exception ex){
            Log.e(Constants.LOG_TAG, ex.getMessage());
            Crashlytics.logException(ex);
        }
    }

    public void hideActivityIndicator() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
        catch (Exception ex){
            Log.e(Constants.LOG_TAG, ex.getMessage());
            Crashlytics.logException(ex);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        accountManager = AccountManager.get(this);
        ((OpenStackSummitApplication) getApplication()).getApplicationComponent().inject(this);

        // Are we supposed to create a new account or renew the authorisation of an old one?
        isNewAccount = getIntent().getBooleanExtra(KEY_IS_NEW_ACCOUNT, false);

        // In case we're renewing authorisation, we also got an Account object that we're supposed
        // to work with.
        account = getIntent().getParcelableExtra(KEY_ACCOUNT_OBJECT);

        // Fetch the authentication URL that was given to us by the calling activity
        String authUrl = getIntent().getStringExtra(KEY_AUTH_URL) + "&nonce=" + String.valueOf(new Date().getTime());

        Log.d(Constants.LOG_TAG, String.format("Initiated activity for getting authorization with URL '%s'.", authUrl));

        // Initialise the WebView
        WebView webView = (WebView) findViewById(R.id.WebView);

        webView.setWebViewClient(new CustomWebViewClient(this, this.oidcConfigurationManager));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        CookieManager cookieManager = CookieManager.getInstance();
        webView.clearCache(true);
        webView.clearHistory();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            cookieManager.setAcceptThirdPartyCookies(webView, true);
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
            webView.getSettings().setAllowFileAccessFromFileURLs(true);
            cookieManager.removeAllCookies(null);
        }
        else{
            cookieManager.removeAllCookie();
        }

        cookieManager.setAcceptCookie(true);

        webView.loadUrl(authUrl);
    }

    /**
     * CustomWebViewClient
     */
    private static class CustomWebViewClient extends WebViewClient {

        private final String TAG = getClass().getSimpleName();

        private WeakReference<AuthenticatorActivity> authActivity;
        private OIDCNativeClientConfiguration clientConfig;
        private IOIDCConfigurationManager oidcConfigurationManager;

        public CustomWebViewClient(AuthenticatorActivity activity, IOIDCConfigurationManager oidcConfigurationManager) {
            authActivity = new WeakReference<>(activity);
            this.oidcConfigurationManager = oidcConfigurationManager;
            clientConfig = (OIDCNativeClientConfiguration) oidcConfigurationManager.buildConfiguration(OIDCClientConfiguration.ODICAccountType.NativeAccount);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            WebView webView = (WebView) authActivity.get().findViewById(R.id.WebView);
            authActivity.get().hideActivityIndicator();
            webView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(authActivity.get());
            builder.setMessage(R.string.notification_error_ssl_cert_invalid);
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            authActivity.get().hideActivityIndicator();
            WebView webView = (WebView) authActivity.get().findViewById(R.id.WebView);
            webView.setVisibility(View.VISIBLE);


            String cookies = CookieManager.getInstance().getCookie(url);
            if (cookies != null) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    flushCookies();
                else CookieSyncManager.getInstance().sync();
            }
        }

        @TargetApi(21)
        private void flushCookies() {
            CookieManager.getInstance().flush();
        }

        @Override
        public void onPageStarted(WebView view, String urlString, Bitmap favicon) {
            try {
                super.onPageStarted(view, urlString, favicon);

                WebView webView           = (WebView) authActivity.get().findViewById(R.id.WebView);

                ISession session           = authActivity.get().session;
                Uri url                    = Uri.parse(urlString);
                Set<String> parameterNames = url.getQueryParameterNames();
                String extractedFragment   = url.getEncodedFragment();

                webView.setVisibility(View.INVISIBLE);
                authActivity.get().showActivityIndicator();

                if (parameterNames.contains("error")) {
                    view.stopLoading();

                    // In case of an error, the `error` parameter contains an ASCII identifier, e.g.
                    // "temporarily_unavailable" and the `error_description` *may* contain a
                    // human-readable description of the error.
                    //
                    // For a list of the error identifiers, see
                    // http://tools.ietf.org/html/rfc6749#section-4.1.2.1

                    String error            = url.getQueryParameter("error");
                    String errorDescription = url.getQueryParameter("error_description");
                    Dialog dialog = AlertsBuilder.buildRawAlert(authActivity.get(), String.format("Error code: %s", error));
                    if(dialog != null) dialog.show();

                    return;
                }

                if (urlString.startsWith(clientConfig.getReturnUrl())) {
                    // We won't need to keep loading anymore. This also prevents errors when using
                    // redirect URLs that don't have real protocols (like app://) that are just
                    // used for identification purposes in native apps.
                    view.stopLoading();
                    try {
                        switch (clientConfig.getFlowType()) {
                            case Implicit: {
                                if (TextUtils.isEmpty(extractedFragment)) {
                                    throw new InvalidParameterException(String.format(
                                            "urlString '%1$s' doesn't contain fragment part; can't extract tokens",
                                            urlString));
                                }
                                CreateIdTokenFromFragmentPartTask task = new CreateIdTokenFromFragmentPartTask(authActivity.get());
                                task.execute(extractedFragment);
                                break;
                            }
                            case Hybrid: {
                                if (TextUtils.isEmpty(extractedFragment)) {
                                    throw new InvalidParameterException(String.format(
                                            "urlString '%1$s' doesn't contain fragment part; can't extract tokens",
                                            urlString));
                                }
                                RequestIdTokenFromFragmentPartTask task = new RequestIdTokenFromFragmentPartTask(authActivity.get(), oidcConfigurationManager);
                                task.execute(extractedFragment);
                                break;
                            }
                            case AuthorizationCode:
                            default: {
                                // The URL will contain a `code` parameter when the user has been authenticated
                                if (!parameterNames.contains("code")) {
                                    throw new InvalidParameterException
                                    (
                                        String.format
                                        (
                                            "urlString '%1$s' doesn't contain code param; can't extract authCode",
                                            urlString
                                        )
                                    );
                                }
                                String authCode       = url.getQueryParameter("code");
                                String formerAuthCode = session.getString(LAST_AUTH_CODE);
                                // avoid double redeem of code.
                                if(formerAuthCode != null && authCode != null && formerAuthCode.equals(authCode))
                                    return;
                                session.setString(LAST_AUTH_CODE, authCode);
                                // Request the ID token
                                RequestIdTokenTask task = new RequestIdTokenTask(authActivity.get(), oidcConfigurationManager);
                                task.execute(authCode);
                                break;
                            }
                        }
                    } catch (InvalidParameterException ipEx) {
                        Log.e(TAG, ipEx.getMessage());
                    }
                }
                // else : should be an intermediate url, load it and keep going
            }
            catch(UnsupportedOperationException ex1){
                view.stopLoading();
                Crashlytics.log(ex1.getMessage()+ " URL "+urlString);
                Dialog dialog = AlertsBuilder.buildRawAlert(authActivity.get(), authActivity.get().getResources().getString(R.string.login_error_message));
                if(dialog != null) dialog.show();
                Log.e(Constants.LOG_TAG, ex1.getMessage());
            }
            catch(Exception ex){
                view.stopLoading();
                Crashlytics.logException(ex);
                Dialog dialog = AlertsBuilder.buildRawAlert(authActivity.get(), authActivity.get().getResources().getString(R.string.login_error_message));
                if(dialog != null) dialog.show();
                Log.e(Constants.LOG_TAG, ex.getMessage());
            }
        }
    }

    /**
     * Implicit Flow
     */
    private static class CreateIdTokenFromFragmentPartTask extends AsyncTask<String, Void, Boolean> {

        private final String TAG = getClass().getSimpleName();
        private WeakReference<AuthenticatorActivity> authActivity;

        public CreateIdTokenFromFragmentPartTask(AuthenticatorActivity activity) {
            super();
            authActivity = new WeakReference<>(activity);
        }

        @Override
        protected Boolean doInBackground(String... args) {
            try {
                
                String fragmentPart = args[0];

                Uri tokenExtrationUrl = new Uri.Builder().encodedQuery(fragmentPart).build();
                String accessToken = tokenExtrationUrl.getQueryParameter("access_token");
                String idToken = tokenExtrationUrl.getQueryParameter("id_token");
                String tokenType = tokenExtrationUrl.getQueryParameter("token_type");
                String expiresInString = tokenExtrationUrl.getQueryParameter("expires_in");
                Long expiresIn = (!TextUtils.isEmpty(expiresInString)) ? Long.decode(expiresInString) : null;

                String scope = tokenExtrationUrl.getQueryParameter("scope");

                if (TextUtils.isEmpty(accessToken) || TextUtils.isEmpty(idToken) || TextUtils.isEmpty(tokenType) || expiresIn == null) {
                    return false;
                } else {
                    Log.i(TAG, "AuthToken : " + accessToken);

                    IdTokenResponse response = new IdTokenResponse();
                    response.setAccessToken(accessToken);
                    response.setIdToken(idToken);
                    response.setTokenType(tokenType);
                    response.setExpiresInSeconds(expiresIn);
                    response.setScope(scope);
                    response.setFactory(new GsonFactory());

                    if (authActivity.get().isNewAccount) {
                        authActivity.get().createAccount(response);
                    } else {
                        authActivity.get().setTokens(response);
                    }
                }
            } catch (Exception e) {
                Crashlytics.logException(e);
                Log.e(Constants.LOG_TAG, "Error executing API request", e);
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean wasSuccess) {
            if (!wasSuccess) {
                Dialog dialog = AlertsBuilder.buildRawAlert(authActivity.get(), "Could not get ID Token.");
                if(dialog != null) dialog.show();
                return;
            }
            // The account manager still wants the following information back
            Intent intent = new Intent();

            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, authActivity.get().account.name);
            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, authActivity.get().account.type);

            authActivity.get().setAccountAuthenticatorResult(intent.getExtras());
            authActivity.get().setResult(RESULT_OK, intent);
            authActivity.get().finish();

        }
    }

    /**
     * Hybrid flow
     */
    private static class RequestIdTokenFromFragmentPartTask extends AsyncTask<String, Void, Boolean> {

        private OIDCNativeClientConfiguration clientConfig;
        private OpenIdConnectProtocol oidcProtocol;
        private final String TAG = getClass().getSimpleName();
        private WeakReference<AuthenticatorActivity> authActivity;

        public RequestIdTokenFromFragmentPartTask(AuthenticatorActivity activity, IOIDCConfigurationManager oidcConfigurationManager) {
            super();
            authActivity = new WeakReference<>(activity);
            clientConfig = (OIDCNativeClientConfiguration) oidcConfigurationManager.buildConfiguration(OIDCClientConfiguration.ODICAccountType.NativeAccount);
            oidcProtocol = new OpenIdConnectProtocol(oidcConfigurationManager.buildIdentityProviderUrls());
        }

        @Override
        protected Boolean doInBackground(String... args) {
            try {
                String fragmentPart = args[0];

                Uri tokenExtrationUrl = new Uri.Builder().encodedQuery(fragmentPart).build();
                String idToken = tokenExtrationUrl.getQueryParameter("id_token");
                String authCode = tokenExtrationUrl.getQueryParameter("code");

                if (TextUtils.isEmpty(idToken) || TextUtils.isEmpty(authCode)) {
                    return false;
                } else {
                    IdTokenResponse response;

                    Log.i(TAG, "Requesting access_token with AuthCode : " + authCode);

                    try {
                        response = oidcProtocol.makeTokenRequest(new AuthCodeResponse(clientConfig, authCode));
                    } catch (Exception e) {
                        Log.e(TAG, "Could not get response.");
                        Crashlytics.logException(e);
                        return false;
                    }

                    if (authActivity.get().isNewAccount) {
                        authActivity.get().createAccount(response);
                    } else {
                        authActivity.get().setTokens(response);
                    }
                }
            } catch (Exception e) {
                Crashlytics.logException(e);
                Log.e(Constants.LOG_TAG, "Error executing API request", e);
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean wasSuccess) {
            if (!wasSuccess) {
                Dialog dialog = AlertsBuilder.buildRawAlert(authActivity.get(), "Could not get ID Token.");
                if(dialog != null) dialog.show();
                return;
            }
            // The account manager still wants the following information back
            Intent intent = new Intent();

            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, authActivity.get().account.name);
            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, authActivity.get().account.type);

            authActivity.get().setAccountAuthenticatorResult(intent.getExtras());
            authActivity.get().setResult(RESULT_OK, intent);
            authActivity.get().finish();

        }
    }

    /**
     * Requests the ID Token asynchronously.
     */
    private static class RequestIdTokenTask extends AsyncTask<String, Void, Boolean> {

        private OIDCNativeClientConfiguration clientConfig;
        private OpenIdConnectProtocol oidcProtocol;
        private WeakReference<AuthenticatorActivity> authActivity;

        public RequestIdTokenTask(AuthenticatorActivity activity, IOIDCConfigurationManager oidcConfigurationManager) {
            super();
            authActivity = new WeakReference<>(activity);
            clientConfig = (OIDCNativeClientConfiguration) oidcConfigurationManager.buildConfiguration(OIDCClientConfiguration.ODICAccountType.NativeAccount);
            oidcProtocol = new OpenIdConnectProtocol(oidcConfigurationManager.buildIdentityProviderUrls());
        }

        @Override
        protected Boolean doInBackground(String... args) {
            try {
                String authCode = args[0];
                IdTokenResponse response;

                Log.d(Constants.LOG_TAG, "RequestIdTokenTask : Requesting ID token.");

                try {
                    response = oidcProtocol.makeTokenRequest(new AuthCodeResponse(clientConfig, authCode));
                } catch (IOException e) {
                    Crashlytics.logException(e);
                    Log.e(Constants.LOG_TAG, e.getMessage(), e);
                    return false;
                }

                AuthenticatorActivity activity = authActivity.get();
                if(activity == null) return false;

                if (activity.isNewAccount) {
                    activity.createAccount(response);
                } else {
                    activity.setTokens(response);
                }

                activity.setWorkflowCompleted();
            } catch (Exception e) {
                Crashlytics.logException(e);
                Log.e(Constants.LOG_TAG, "Error executing API request", e);
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean wasSuccess) {

            AuthenticatorActivity activity = authActivity.get();
            if(activity == null) return;

            if (!wasSuccess) {
                Dialog dialog = AlertsBuilder.buildRawAlert(authActivity.get(), "Could not get ID Token.");
                if(dialog != null) dialog.show();
                return;
            }
            // The account manager still wants the following information back
            Intent intent = new Intent();

            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, authActivity.get().account.name);
            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, authActivity.get().account.type);
            Log.d(Constants.LOG_TAG, "RequestIdTokenTask.onPostExecute");
            activity.setAccountAuthenticatorResult(intent.getExtras());
            activity.setResult(RESULT_OK, intent);
            activity.finish();
        }
    }

    private void createAccount(IdTokenResponse response) {
        Log.d(Constants.LOG_TAG, "Creating account.");

        String accountType = getString(R.string.ACCOUNT_TYPE);

        // AccountManager expects that each account has a unique username. If a new account has the
        // same username as a previously created one, it will overwrite the older account.
        //
        // Unfortunately the OIDC spec cannot guarantee[1] that any user information is unique,
        // save for the user ID (i.e. the ID Token subject) which is hardly human-readable. This
        // makes choosing between multiple accounts difficult.
        //
        // We'll resort to naming each account `preferred_username (ID)`. This is a neat solution
        // if the user ID is short enough.
        //
        // [1]: http://openid.net/specs/openid-connect-basic-1_0.html#ClaimStability

        // Use the app name as a fallback if the other information isn't available for some reason.
        String accountName = getString(R.string.app_name);
        String accountId = null;

        try {
            accountId = response.parseIdToken().getPayload().getSubject();
        } catch (IOException e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, "Could not get ID Token subject.");
        }

        account = new Account(String.format("%s (%s)", accountName, accountId), accountType);
        accountManager.addAccountExplicitly(account, null, null);

        // Store the tokens in the account
        setTokens(response);

        Log.d(Constants.LOG_TAG, "Account created.");
    }

    private void setTokens(IdTokenResponse response) {
        accountManager.setAuthToken(account, Authenticator.TOKEN_TYPE_ID, response.getIdToken());
        accountManager.setAuthToken(account, Authenticator.TOKEN_TYPE_ACCESS, response.getAccessToken());
        accountManager.setAuthToken(account, Authenticator.TOKEN_TYPE_REFRESH, response.getRefreshToken());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Constants.LOG_TAG, "AuthenticatorActivity.onDestroy");
        hideActivityIndicator();
        if (!workflowCompleted) {
            // was canceled ... inform it
            Intent intent = new Intent(Constants.LOG_IN_CANCELLED_EVENT);
            LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);
        }
    }
}

