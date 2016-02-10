package org.openstack.android.summit.common.data_access.data_polling;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.api.client.http.HttpMethods;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.IDataUpdateDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.network.HttpTask;
import org.openstack.android.summit.common.network.HttpTaskListener;
import org.openstack.android.summit.common.network.IHttpTaskFactory;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.AccountType;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.security.spec.InvalidParameterSpecException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Claudio Redi on 2/5/2016.
 */
public class DataUpdatePoller implements IDataUpdatePoller {
    private int pollingInterval = 30;
    private ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
    private ScheduledFuture<?> scheduledFuture;
    private ISecurityManager securityManager;
    private IHttpTaskFactory httpTaskFactory;
    private IDataUpdateProcessor dataUpdateProcessor;
    private IDataUpdateDataStore dataUpdateDataStore;
    private ISummitDataStore summitDataStore;
    private IReachability reachability;
    private int fromDate;
    private String KEY_SET_FROM_DATE = "KEY_SET_FROM_DATE";
    private String url;

    public DataUpdatePoller(ISecurityManager securityManager, IHttpTaskFactory httpTaskFactory, IDataUpdateProcessor dataUpdateProcessor, IDataUpdateDataStore dataUpdateDataStore, ISummitDataStore summitDataStore, IReachability reachability) {
        this.securityManager = securityManager;
        this.httpTaskFactory = httpTaskFactory;
        this.dataUpdateProcessor = dataUpdateProcessor;
        this.dataUpdateDataStore = dataUpdateDataStore;
        this.summitDataStore = summitDataStore;
        this.reachability = reachability;
    }

    @Override
    public void pollServer() {
        try {
        /*if (!reachability.isNetworkingAvailable(OpenStackSummitApplication.context)) {
            return;
        }*/

            Log.d(Constants.LOG_TAG, "Polling server for data updates");

            String url = getUrl();

            if (url == null) {
                return;
            }

            HttpTaskListener taskListener = new HttpTaskListener() {
                @Override
                public void onSucceed(String data) {
                    Log.d(Constants.LOG_TAG, "Data updates: " + data);

                    try {
                        dataUpdateProcessor.process(data);
                    }
                    catch (Exception ex) {
                        Log.e(Constants.LOG_TAG, "There was an error processing updates from server: " + ex.getMessage(), ex);
                    }

                }

                @Override
                public void onError(String error) {
                    Log.d(Constants.LOG_TAG, String.format("Error polling server for data updates: %s", error));
                }
            };

            HttpTask httpTask = null;
            try {
                httpTask = securityManager.isLoggedIn()
                        ? httpTaskFactory.create(AccountType.OIDC, url, HttpRequest.METHOD_GET, taskListener)
                        : httpTaskFactory.create(AccountType.ServiceAccount, url, HttpRequest.METHOD_GET, taskListener);
            } catch (InvalidParameterSpecException e) {
                Log.d(Constants.LOG_TAG, e.getMessage(), e);
            }
            httpTask.execute();
        }
        catch (Exception e) {
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            //TODO: fabric
        }
    }

    public void startPollingIfNotPollingAlready() {
        try {
            if (scheduledFuture != null) {
                return;
            }
            ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
            scheduledFuture = scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    pollServer();
                }
            }, 0, pollingInterval, TimeUnit.SECONDS);
        }
        catch (Exception e) {
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            //TODO: fabric
        }
    }

    private long getFromDate() {
        SharedPreferences preferences = OpenStackSummitApplication.context.getSharedPreferences(Constants.LOG_TAG, Context.MODE_PRIVATE);
        return preferences.getLong(KEY_SET_FROM_DATE, 0);
    }

    private void setFromDate(long fromDate) {
        SharedPreferences preferences = OpenStackSummitApplication.context.getSharedPreferences(Constants.LOG_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(KEY_SET_FROM_DATE, fromDate);
        editor.apply();
    }

    public String getUrl() {
        String url = null;
        int latestDataUpdateId = dataUpdateDataStore.getLatestDataUpdate();
        if (latestDataUpdateId > 0) {
            url = String.format("%s%s%d", Constants.RESOURCE_SERVER_BASE_URL, "/api/v1/summits/current/entity-events?last_event_id=", latestDataUpdateId);
        }
        else {
            long fromDate = getFromDate();
            if (fromDate == 0) {
                Summit summit = summitDataStore.getActiveLocal();
                if (summit != null) {
                    fromDate = summit.getInitialDataLoadDate().getTime() / 1000L;
                    setFromDate(fromDate);
                }
            }

            if (fromDate != 0) {
                url = String.format("%s%s%d", Constants.RESOURCE_SERVER_BASE_URL, "/api/v1/summits/current/entity-events?from_date=", fromDate);
            }
        }

        return url;
    }

    public void stop() {
        scheduledFuture.cancel(true);
    }
}