package org.openstack.android.summit.common.data_access.data_polling;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.github.kevinsawicki.http.HttpRequest;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api_endpoints.ApiEndpointBuilder;
import org.openstack.android.summit.common.data_access.BaseRemoteDataStore;
import org.openstack.android.summit.common.data_access.IDataUpdateDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.network.AuthorizationException;
import org.openstack.android.summit.common.network.HttpTask;
import org.openstack.android.summit.common.network.HttpTaskListener;
import org.openstack.android.summit.common.network.IHttpTaskFactory;
import org.openstack.android.summit.common.security.AccountType;
import org.openstack.android.summit.common.security.ISecurityManager;
import java.security.spec.InvalidParameterSpecException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Claudio Redi on 2/5/2016.
 */
public class DataUpdatePoller extends BaseRemoteDataStore implements IDataUpdatePoller {

    private ISecurityManager securityManager;
    private IHttpTaskFactory httpTaskFactory;
    private IDataUpdateProcessor dataUpdateProcessor;
    private IDataUpdateDataStore dataUpdateDataStore;
    private ISummitDataStore summitDataStore;
    private ISession session;

    private static final String KEY_SET_FROM_DATE       = "KEY_SET_FROM_DATE";
    private static final String KEY_LAST_WIPE_EVENT_ID  = "KEY_LAST_WIPE_EVENT_ID";
    private static final String KEY_LAST_EVENT_ID       = "KEY_LAST_EVENT_ID";
    private static final int EntityEventUpdatesPageSize = 100;

    public DataUpdatePoller(ISecurityManager securityManager, IHttpTaskFactory httpTaskFactory, IDataUpdateProcessor dataUpdateProcessor, IDataUpdateDataStore dataUpdateDataStore, ISummitDataStore summitDataStore, ISession session) {

        this.securityManager     = securityManager;
        this.httpTaskFactory     = httpTaskFactory;
        this.dataUpdateProcessor = dataUpdateProcessor;
        this.dataUpdateDataStore = dataUpdateDataStore;
        this.summitDataStore     = summitDataStore;
        this.session             = session;
    }

    @Override
    public void pollServer() {

        try {

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
                        clearDataIfTruncateEventExist();
                    } catch (Exception e) {
                        String errorMessage = String.format("There was an error processing these updates from server: : %s", data);
                        Crashlytics.logException(new Exception(errorMessage, e));
                        Log.e(Constants.LOG_TAG, errorMessage, e);
                    }
                }

                @Override
                public void onError(Throwable error) {
                    if (error instanceof AuthorizationException) {
                        securityManager.handleIllegalState();
                    }
                    Log.d(Constants.LOG_TAG, String.format("Error polling server for data updates: %s", error.getMessage()));
                }
            };

            HttpTask httpTask = securityManager.isLoggedIn()
                    ? httpTaskFactory.create(AccountType.OIDC, url, HttpRequest.METHOD_GET, null, null, taskListener)
                    : httpTaskFactory.create(AccountType.ServiceAccount, url, HttpRequest.METHOD_GET, null, null, taskListener);
            httpTask.execute();
        } catch (InvalidParameterSpecException e) {
            Log.d(Constants.LOG_TAG, e.getMessage(), e);
        } catch (Exception e) {
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

        long latestDataUpdateId       = session.getLong(KEY_LAST_EVENT_ID);
        long latestDataUpdateIdFromDB = dataUpdateDataStore.getLatestDataUpdate();
        Map<String, Object>params     =  new HashMap<>();

        if(latestDataUpdateId < latestDataUpdateIdFromDB ){
            latestDataUpdateId = latestDataUpdateIdFromDB;
            session.setLong(KEY_LAST_EVENT_ID, latestDataUpdateId);
        }

        if (latestDataUpdateId > 0){
            params.put(ApiEndpointBuilder.LimitParam, EntityEventUpdatesPageSize);
            params.put(ApiEndpointBuilder.LastEventIdParam, latestDataUpdateId);
            return ApiEndpointBuilder.getInstance().buildEndpoint(getBaseResourceServerUrl(), "current", ApiEndpointBuilder.EndpointType.EntityEvents, params ).toString();
        }

        long fromDate = getFromDate();
        if (fromDate == 0) {
            Summit summit = summitDataStore.getActiveLocal();
            if (summit != null) {
                fromDate = summit.getInitialDataLoadDate().getTime() / 1000L;
                setFromDate(fromDate);
            }
        }
        if (fromDate != 0) {
            params.put(ApiEndpointBuilder.LimitParam, EntityEventUpdatesPageSize);
            params.put(ApiEndpointBuilder.FromDateParam, fromDate);
            return ApiEndpointBuilder.getInstance().buildEndpoint(getBaseResourceServerUrl(),"current", ApiEndpointBuilder.EndpointType.EntityEvents, params ).toString();
        }

        return null;
    }

    private void clearDataIfTruncateEventExist() {
        DataUpdate dataUpdate = null;
        do {
            long lastWipeEventId = session.getLong(KEY_LAST_WIPE_EVENT_ID);
            dataUpdate           = dataUpdateDataStore.getTruncateDataUpdate();

            if (dataUpdate != null) {
                if (lastWipeEventId == 0 || lastWipeEventId < dataUpdate.getId()) {

                    if(dataUpdateDataStore.getLatestDataUpdate() == 1)
                        session.setLong(KEY_LAST_EVENT_ID, dataUpdate.getId());

                    Log.d(Constants.LOG_TAG, "doing a wipe DB ...");
                    session.setLong(KEY_LAST_WIPE_EVENT_ID, dataUpdate.getId());
                    if (securityManager.isLoggedIn()) {
                        securityManager.logout();
                    }
                    dataUpdateDataStore.clearDataLocal();
                    setFromDate(0L);
                    Intent intent = new Intent(Constants.WIPE_DATE_EVENT);
                    LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);
                    return;
                }
                dataUpdateDataStore.deleteDataUpdate(dataUpdate);
            }
        } while (dataUpdate != null);
    }

}