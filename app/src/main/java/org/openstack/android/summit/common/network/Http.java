package org.openstack.android.summit.common.network;

import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.security.ITokenManager;
import org.openstack.android.summit.common.security.TokenGenerationException;

import java.io.IOException;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

public class Http implements IHttp {
    private ITokenManager tokenManager;

    public Http(ITokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public String GET(String url) throws IOException {
        return makeRequest(HttpRequest.METHOD_GET, url, null, null, getTokenManager(), true);
    }

    @Override
    public String POST(String url, String contentType, String content) throws IOException {
        return makeRequest(HttpRequest.METHOD_POST, url, contentType, content, getTokenManager(), true);
    }

    @Override
    public String DELETE(String url) throws IOException {
        return makeRequest(HttpRequest.METHOD_DELETE, url, null, null, getTokenManager(), true);
    }

    protected String makeRequest(String method, String url, String contentType, String content, ITokenManager tokenManager, boolean doRetry) throws IOException {

        String token = null;
        try {
            token = tokenManager.getToken();
        } catch (TokenGenerationException e) {
            Log.e(Constants.LOG_TAG, "Error getting token", e);
        }
        // Prepare an API request using the token
        HttpRequest request = new HttpRequest(url, method);
        request = prepareApiRequest(request, token);

        if (content != null && !content.isEmpty()) {
            request.contentType(contentType).send(content);
        }

        if (request.ok() || request.code() == HTTP_CREATED || request.code() == HTTP_NO_CONTENT) {
            return request.body();
        } else {
            int code = request.code();

            if (doRetry && (code == HTTP_UNAUTHORIZED || code == HTTP_FORBIDDEN || code == HTTP_BAD_REQUEST)) {
                tokenManager.invalidateToken(token);

                return makeRequest(method, url, contentType, content, tokenManager, false);
            } else {
                // An unrecoverable error or the renewed token didn't work either
                throw new IOException(request.code() + " " + request.message());
            }
        }
    }

    protected  HttpRequest prepareApiRequest(HttpRequest request, String idToken)
            throws IOException {

        return request.authorization("Bearer " + idToken).acceptJson();
    }

    public ITokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(ITokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }
}