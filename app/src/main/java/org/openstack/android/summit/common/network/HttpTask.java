package org.openstack.android.summit.common.network;

import android.os.AsyncTask;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.github.kevinsawicki.http.HttpRequest;

import org.openstack.android.summit.common.Constants;

import java.io.IOException;

/**
 * Created by Claudio Redi on 12/8/2015.
 */
public class HttpTask extends AsyncTask<Void, Void, HttpTaskResult> {
    private HttpTaskConfig config;

    public HttpTask(HttpTaskConfig config) {
        this.config = config;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected HttpTaskResult doInBackground(Void... args) {
        HttpTaskResult taskResult = new HttpTaskResult();
        try {
            String method = config.getMethod();
            if (method == HttpRequest.METHOD_GET) {
                String body = config.getHttp().GET(config.getUrl());
                taskResult.setBody(body);
            }
            else if (method == HttpRequest.METHOD_POST){
                String body = config.getHttp().POST(config.getUrl(), config.getContentType(), config.getContent());
                taskResult.setBody(body);
            }
            else if (method == HttpRequest.METHOD_DELETE){
                String body = config.getHttp().DELETE(config.getUrl());
                taskResult.setBody(body);
            }
            else {
                taskResult.setSucceed(false);
                taskResult.setBody("Invalid http method");
            }
            taskResult.setSucceed(true);

        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, "Error executing API request", e);
            taskResult.setSucceed(false);
            taskResult.setBody(e.getMessage());
            taskResult.setError(e);
        }

        return taskResult;
    }

    /**
     * Processes the API's response.
     */
    @Override
    protected void onPostExecute(HttpTaskResult result) {
        if (config.getDelegate() != null) {
            if (result.getSucceed()) {
                config.getDelegate().onSucceed(result.getBody());
            }
            else {
                config.getDelegate().onError(result.getError());
            }
        }
    }
}