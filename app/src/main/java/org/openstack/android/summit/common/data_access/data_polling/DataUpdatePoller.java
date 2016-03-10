package org.openstack.android.summit.common.data_access.data_polling;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.api.client.http.HttpMethods;

import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.data_access.IDataUpdateDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.network.AuthorizationException;
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
    private int pollingInterval = 30*1000;
    private ISecurityManager securityManager;
    private IHttpTaskFactory httpTaskFactory;
    private IDataUpdateProcessor dataUpdateProcessor;
    private IDataUpdateDataStore dataUpdateDataStore;
    private ISummitDataStore summitDataStore;
    private IReachability reachability;
    private ISession session;
    private int fromDate;
    private String KEY_SET_FROM_DATE = "KEY_SET_FROM_DATE";
    private String url;
    private Handler handler;
    private Runnable dataUpdatePoller;

    public DataUpdatePoller(ISecurityManager securityManager, IHttpTaskFactory httpTaskFactory, IDataUpdateProcessor dataUpdateProcessor, IDataUpdateDataStore dataUpdateDataStore, ISummitDataStore summitDataStore, IReachability reachability, ISession session) {
        this.securityManager = securityManager;
        this.httpTaskFactory = httpTaskFactory;
        this.dataUpdateProcessor = dataUpdateProcessor;
        this.dataUpdateDataStore = dataUpdateDataStore;
        this.summitDataStore = summitDataStore;
        this.reachability = reachability;
        this.session = session;
    }

    @Override
    public void pollServer() {

        try {
            if (!reachability.isNetworkingAvailable(OpenStackSummitApplication.context)) {
                return;
            }

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
                    catch (Exception e) {
                        String errorMessage = String.format("There was an error processing these updates from server: : %s", data);
                        Crashlytics.logException(new Exception(errorMessage, e));
                        Log.e(Constants.LOG_TAG, errorMessage, e);
                    }
                }

                @Override
                public void onError(Throwable error) {
                    if (error instanceof AuthorizationException){
                        securityManager.handleIllegalState();
                    }
                    Log.d(Constants.LOG_TAG, String.format("Error polling server for data updates: %s", error.getMessage()));
                }
            };

            HttpTask httpTask = securityManager.isLoggedIn()
                    ? httpTaskFactory.create(AccountType.OIDC, url, HttpRequest.METHOD_GET, null, null, taskListener)
                    : httpTaskFactory.create(AccountType.ServiceAccount, url, HttpRequest.METHOD_GET, null, null, taskListener);
            httpTask.execute();
        }
        catch (InvalidParameterSpecException e) {
            Log.d(Constants.LOG_TAG, e.getMessage(), e);
        }
        catch (Exception e) {
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            //TODO: fabric
        }
    }

    public void startPollingIfNotPollingAlready() {
        try {
            if (dataUpdatePoller != null) {
                return;
            }

            handler = new Handler();
            dataUpdatePoller = new Runnable() {
                @Override
                public void run() {
                    try {
                        pollServer();
                    } finally {
                        handler.postDelayed(dataUpdatePoller, pollingInterval);
                    }
                }
            };
            dataUpdatePoller.run();
        }
        catch (Exception e) {
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            //TODO: fabric
        }
    }

    private long getFromDate() {
        return session.getLong(KEY_SET_FROM_DATE);
    }

    private void setFromDate(long fromDate) {
        session.setLong(KEY_SET_FROM_DATE, fromDate);
    }

    public String getUrl() {
        String url = null;
        int latestDataUpdateId = dataUpdateDataStore.getLatestDataUpdate();
        if (latestDataUpdateId > 0) {
            url = String.format("%s%s%d", getResourceServerUrl(), "/api/v1/summits/current/entity-events?last_event_id=", latestDataUpdateId);
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
                url = String.format("%s%s%d", getResourceServerUrl(), "/api/v1/summits/current/entity-events?from_date=", fromDate);
            }
        }

        return url;
    }

    public void stop() {
        if (handler != null) {
            handler.removeCallbacks(dataUpdatePoller);
            handler = null;
            dataUpdatePoller = null;
        }
    }

    @Override
    public void clearDataIfTruncateEventExist() {
        DataUpdate dataUpdate = dataUpdateDataStore.getTruncateDataUpdate();
        if (dataUpdate != null) {
            dataUpdateDataStore.clearDataLocal();
            setFromDate(0L);
            if (securityManager.isLoggedIn()) {
                securityManager.logout();
            }
        }
    }

    private String getResourceServerUrl() {
        String resourceServerUrl = "";
        if (BuildConfig.DEBUG) {
            resourceServerUrl = Constants.TEST_RESOURCE_SERVER_BASE_URL;
        }
        else {
            resourceServerUrl = Constants.PRODUCTION_RESOURCE_SERVER_BASE_URL;
        }
        return resourceServerUrl;
    }
}