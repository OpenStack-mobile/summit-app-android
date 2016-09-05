package org.openstack.android.summit.common.security;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.crashlytics.android.Crashlytics;
import com.google.api.client.auth.openidconnect.IdTokenResponse;
import com.google.api.client.json.gson.GsonFactory;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.security.oidc.AuthCodeResponse;
import org.openstack.android.summit.common.security.oidc.OpenIdConnectProtocol;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.Set;

import javax.inject.Inject;

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
 *
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    public static final String KEY_AUTH_URL       = "org.openstack.android.summit.KEY_AUTH_URL";
    public static final String KEY_IS_NEW_ACCOUNT = "org.openstack.android.summit.KEY_IS_NEW_ACCOUNT";
    public static final String KEY_ACCOUNT_OBJECT = "org.openstack.android.summit.KEY_ACCOUNT_OBJECT";

    private final String TAG = getClass().getSimpleName();
    private AccountManager accountManager;
    private Account account;
    private boolean isNewAccount;

    @Inject
    IOIDCConfigurationManager oidcConfigurationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        accountManager = AccountManager.get(this);
        ((OpenStackSummitApplication) getApplication()).getApplicationComponent().inject(this);
        Bundle extras = getIntent().getExtras();

        // Are we supposed to create a new account or renew the authorisation of an old one?
        isNewAccount = extras.getBoolean(KEY_IS_NEW_ACCOUNT, false);

        // In case we're renewing authorisation, we also got an Account object that we're supposed
        // to work with.
        account = extras.getParcelable(KEY_ACCOUNT_OBJECT);

        // Fetch the authentication URL that was given to us by the calling activity
        String authUrl = extras.getString(KEY_AUTH_URL) + "&nonce=" + String.valueOf(new Date().getTime());

        Log.d(TAG, String.format("Initiated activity for getting authorisation with URL '%s'.",
                authUrl));

        // Initialise the WebView
        WebView webView = (WebView) findViewById(R.id.WebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new CustomWebViewClient(this, this.oidcConfigurationManager));
        // persists the oookies ...
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
        CookieManager.getInstance().setAcceptCookie(true);
        webView.loadUrl(authUrl);
    }

    /**
     *  CustomWebViewClient
     */
    private static  class CustomWebViewClient extends WebViewClient {

        private final String TAG = getClass().getSimpleName();

        private WeakReference<AuthenticatorActivity> authActivity;
        private OIDCNativeClientConfiguration clientConfig;
        private IOIDCConfigurationManager oidcConfigurationManager;

        public CustomWebViewClient(AuthenticatorActivity activity, IOIDCConfigurationManager oidcConfigurationManager) {
            authActivity                  = new WeakReference<>(activity);
            this.oidcConfigurationManager = oidcConfigurationManager;
            clientConfig                  = (OIDCNativeClientConfiguration) oidcConfigurationManager.buildConfiguration(OIDCClientConfiguration.ODICAccountType.NativeAccount);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public void onPageFinished(WebView view, String url){
            super.onPageFinished(view, url);
            String cookies = CookieManager.getInstance().getCookie(url);
            if(cookies != null) {
                Log.d(TAG, "All the cookies in a string:" + cookies);
            }
        }

        @TargetApi(21)
        private void flushCookies() {

            CookieManager.getInstance().flush();
        }

        @Override
        public void onPageStarted(WebView view, String urlString, Bitmap favicon) {

            super.onPageStarted(view, urlString, favicon);

            Uri url                    = Uri.parse(urlString);
            Set<String> parameterNames = url.getQueryParameterNames();
            String extractedFragment   = url.getEncodedFragment();

            if (parameterNames.contains("error")) {
                view.stopLoading();

                // In case of an error, the `error` parameter contains an ASCII identifier, e.g.
                // "temporarily_unavailable" and the `error_description` *may* contain a
                // human-readable description of the error.
                //
                // For a list of the error identifiers, see
                // http://tools.ietf.org/html/rfc6749#section-4.1.2.1

                String error = url.getQueryParameter("error");
                String errorDescription = url.getQueryParameter("error_description");

                // If the user declines to authorise the app, there's no need to show an error
                // message.
                if (!error.equals("access_denied")) {
                    authActivity.get().showErrorDialog(String.format("Error code: %s\n\n%s", error,
                            errorDescription));
                }
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
                                throw new InvalidParameterException(String.format(
                                        "urlString '%1$s' doesn't contain code param; can't extract authCode",
                                        urlString));
                            }
                            // Request the ID token
                            RequestIdTokenTask task = new RequestIdTokenTask(authActivity.get(), oidcConfigurationManager);
                            task.execute(url.getQueryParameter("code"));
                            break;
                        }
                    }
                }
                catch (InvalidParameterException ipEx){
                    Log.e(TAG, ipEx.getMessage());
                }
            }
            // else : should be an intermediate url, load it and keep going
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
                authActivity.get().showErrorDialog("Could not get ID Token.");
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
                authActivity.get().showErrorDialog("Could not get ID Token.");
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
        private final String TAG = getClass().getSimpleName();
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
                String authToken = args[0];
                IdTokenResponse response;

                Log.d(TAG, "Requesting ID token.");

                try {
                    response = oidcProtocol.makeTokenRequest(new AuthCodeResponse(clientConfig, authToken));
                } catch (IOException e) {
                    Crashlytics.logException(e);
                    Log.e(TAG, e.getMessage(), e);
                    return false;
                }

                if (authActivity.get().isNewAccount) {
                    authActivity.get().createAccount(response);
                } else {
                    authActivity.get().setTokens(response);
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
                authActivity.get().showErrorDialog("Could not get ID Token.");
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

    private void createAccount(IdTokenResponse response) {
        Log.d(TAG, "Creating account.");

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
            Log.e(TAG, "Could not get ID Token subject.");
        }

        account = new Account(String.format("%s (%s)", accountName, accountId), accountType);
        accountManager.addAccountExplicitly(account, null, null);

        // Store the tokens in the account
        setTokens(response);

        Log.d(TAG, "Account created.");
    }

    private void setTokens(IdTokenResponse response) {
        accountManager.setAuthToken(account, Authenticator.TOKEN_TYPE_ID, response.getIdToken());
        accountManager.setAuthToken(account, Authenticator.TOKEN_TYPE_ACCESS, response.getAccessToken());
        accountManager.setAuthToken(account, Authenticator.TOKEN_TYPE_REFRESH, response.getRefreshToken());
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(AuthenticatorActivity.this)
                .setTitle("Sorry, there was an error")
                .setMessage(message)
                .setCancelable(true)
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                })
                .create()
                .show();
    }

}

