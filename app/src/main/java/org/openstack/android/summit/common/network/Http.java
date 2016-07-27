package org.openstack.android.summit.common.network;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
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

    private class HttpRequestConfig {

        public HttpRequestConfig(String method, String url, String contentType, String content, ITokenManager tokenManager, boolean doRetry, boolean useGZIP) {
            this.method       = method;
            this.url          = url;
            this.contentType  = contentType;
            this.content      = content;
            this.tokenManager = tokenManager;
            this.doRetry      = doRetry;
            this.useGZIP      = useGZIP;
        }

        private String method;
        private String url;
        private String contentType;
        private String content;
        private ITokenManager tokenManager;
        private boolean doRetry;
        private boolean useGZIP;

        public String getMethod() {
            return method;
        }

        public String getUrl() {
            return url;
        }

        public String getContentType() {
            return contentType;
        }

        public String getContent() {
            return content;
        }

        public ITokenManager getTokenManager() {
            return tokenManager;
        }

        public boolean isDoRetry() {
            return doRetry;
        }

        public HttpRequestConfig setDoRetry(boolean doRetry){
            this.doRetry = doRetry;
            return this;
        }

        public boolean isUseGZIP() {
            return useGZIP;
        }
    }

    private ITokenManager tokenManager;

    public Http(ITokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public String GET(String url) throws IOException, AuthorizationException {
        return makeRequest(new HttpRequestConfig(HttpRequest.METHOD_GET, url, null, null, getTokenManager(), true, true));
    }

    @Override
    public String POST(String url, String contentType, String content) throws IOException, AuthorizationException {
        return makeRequest(new HttpRequestConfig(HttpRequest.METHOD_POST, url, contentType, content, getTokenManager(), true, false));
    }

    @Override
    public String DELETE(String url) throws IOException, AuthorizationException {
        return makeRequest(new HttpRequestConfig(HttpRequest.METHOD_DELETE, url, null, null, getTokenManager(), true, false));
    }

    protected String makeRequest(HttpRequestConfig requestConfig) throws IOException, AuthorizationException {

        String token = null;
        try {
            token = requestConfig.getTokenManager().getToken();
        } catch (TokenGenerationException e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, "Error getting token", e);
            throw new AuthorizationException(e);
        }

        if (token == null) {
            throw new AuthorizationException("Token is null");
        }
        // Prepare an API request using the token
        HttpRequest request = new HttpRequest(requestConfig.getUrl(), requestConfig.getMethod());

        if(requestConfig.isUseGZIP()){
            //Tell server to gzip response and automatically uncompress
            request.acceptGzipEncoding().uncompress(true);
        }

        request = prepareApiRequest(request, token);

        if (requestConfig.getContent() != null && !requestConfig.getContent().isEmpty()) {
            request.contentType(requestConfig.getContentType()).send(requestConfig.getContent());
        }

        if (request.ok() || request.code() == HTTP_CREATED || request.code() == HTTP_NO_CONTENT) {
            return request.body();
        } else {
            int code = request.code();

            if (requestConfig.isDoRetry() && (code == HTTP_UNAUTHORIZED || code == HTTP_FORBIDDEN || code == HTTP_BAD_REQUEST)) {
                requestConfig.getTokenManager().invalidateToken(token);
                return makeRequest(requestConfig.setDoRetry(false));
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