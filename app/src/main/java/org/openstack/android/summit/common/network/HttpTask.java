package org.openstack.android.summit.common.network;

import android.os.AsyncTask;

import com.github.kevinsawicki.http.HttpRequest;

import java.io.IOException;

/**
 * Created by Claudio Redi on 12/8/2015.
 */
public class HttpTask extends AsyncTask<HttpTaskConfig, Void, HttpTaskResult> {
    private HttpTaskListener delegate;



    @Override
    protected void onPreExecute() {
    }

    @Override
    protected HttpTaskResult doInBackground(HttpTaskConfig... args) {
        HttpTaskConfig config = args[0];
        delegate = config.getDelegate();
        HttpTaskResult taskResult = new HttpTaskResult();
        try {
            String method = config.getMethod();
            if (method == HttpRequest.METHOD_GET) {
                String body = APIUtility.GET(config.getContext(), config.getUrl(), config.getTokenManager());
                taskResult.setSucceed(true);
                taskResult.setBody(body);
            }
            else {
                taskResult.setSucceed(false);
                taskResult.setBody("Invalid http method");
            }
        } catch (IOException e) {
            e.printStackTrace();
            taskResult.setSucceed(false);
            taskResult.setBody(e.getMessage());
        }

        return taskResult;
    }

    /**
     * Processes the API's response.
     */
    @Override
    protected void onPostExecute(HttpTaskResult result) {
        if (delegate != null) {
            if (result.getSucceed()) {
                delegate.onSucceed(result.getBody());
            }
            else {
                delegate.onError(result.getBody());
            }
        }
    }
}
