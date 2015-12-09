package org.openstack.android.summit.common.network;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.os.Bundle;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.security.Authenticator;
import org.openstack.android.summit.common.security.ITokenManager;
import org.openstack.android.summit.common.security.OIDCUtils;
import org.openstack.android.summit.common.security.TokenManagerOIDC;

import java.io.IOException;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * An incomplete class that illustrates how to make API requests with the ID Token.
 *
 * @author Leo Nikkilä
 */
public class APIUtility {
    /**
     * Makes an arbitrary HTTP request using the provided account.
     *
     * If the request doesn't execute successfully on the first try, the tokens will be refreshed
     * and the request will be retried. If the second try fails, an exception will be raised.
     */
    public static String GET(Context context, String url, ITokenManager tokenManager)
            throws IOException {

        return makeRequest(context, HttpRequest.METHOD_GET, url, tokenManager, true);
    }

    private static String makeRequest(Context context, String method, String url, ITokenManager tokenManager, boolean doRetry) throws IOException {

        AccountManager accountManager = AccountManager.get(context);
        String token = null;
        try {
            token = tokenManager.getToken();
        } catch (AuthenticatorException e) {
            e.printStackTrace();
        } catch (OperationCanceledException e) {
            e.printStackTrace();
        }
        // Prepare an API request using the token
        HttpRequest request = new HttpRequest(url, method);
        request = OIDCUtils.prepareApiRequest(request, token);

        if (request.ok()) {
            return request.body();
        } else {
            int code = request.code();

            if (doRetry && (code == HTTP_UNAUTHORIZED || code == HTTP_FORBIDDEN)) {
                // We're being denied access on the first try, let's renew the token and retry
                String accountType = context.getString(R.string.ACCOUNT_TYPE);
                accountManager.invalidateAuthToken(accountType, token);

                return makeRequest(context, method, url, tokenManager, false);
            } else {
                // An unrecoverable error or the renewed token didn't work either
                throw new IOException(request.code() + " " + request.message());
            }
        }
    }
}