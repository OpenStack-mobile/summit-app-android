package org.openstack.android.summit.common.network;

import android.accounts.Account;
import android.app.Activity;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.api.client.http.HttpMethods;

import org.openstack.android.summit.common.security.ITokenManager;

/**
 * Created by Claudio Redi on 12/8/2015.
 */
public class HttpTaskConfig {
    private String url;
    private String method = "GET";
    private String content;
    private String contentType;
    private ITokenManager tokenManager;
    private HttpTaskListener delegate;
    private IHttp http;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public HttpTaskListener getDelegate() {
        return delegate;
    }

    public void setDelegate(HttpTaskListener delegate) {
        this.delegate = delegate;
    }

    public IHttp getHttp() {
        return http;
    }

    public void setHttp(IHttp http) {
        this.http = http;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
