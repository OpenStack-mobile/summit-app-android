package org.openstack.android.summit.common.data_access.data_polling;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api_endpoints.ApiEndpointBuilder;
import org.openstack.android.summit.common.data_access.BaseRemoteDataStore;
import org.openstack.android.summit.common.data_access.IDataUpdateDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.network.IHttpFactory;
import org.openstack.android.summit.common.security.AccountType;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.security.ITokenManager;
import org.openstack.android.summit.common.security.ITokenManagerFactory;
import org.openstack.android.summit.common.security.TokenManagerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Claudio Redi on 2/5/2016.
 */
public class DataUpdatePoller extends BaseRemoteDataStore implements IDataUpdatePoller {

    private ISecurityManager securityManager;
    private ITokenManagerFactory tokenManagerFactory;
    private IDataUpdateProcessor dataUpdateProcessor;
    private IDataUpdateDataStore dataUpdateDataStore;
    private ISummitDataStore summitDataStore;
    private ISession session;
    private IHttpFactory httpFactory;

    private static final int EntityEventUpdatesPageSize = 50;

    public DataUpdatePoller(ISecurityManager securityManager, ITokenManagerFactory tokenManagerFactory, IDataUpdateProcessor dataUpdateProcessor, IDataUpdateDataStore dataUpdateDataStore, ISummitDataStore summitDataStore, ISession session, IHttpFactory httpFactory) {

        this.securityManager     = securityManager;
        this.tokenManagerFactory = tokenManagerFactory;
        this.dataUpdateProcessor = dataUpdateProcessor;
        this.dataUpdateDataStore = dataUpdateDataStore;
        this.summitDataStore     = summitDataStore;
        this.session             = session;
        this.httpFactory         = httpFactory;
    }

    @Override
    public void pollServer() {

        try {

            Log.d(Constants.LOG_TAG, "Polling server for data updates");

            String url = getUrl();

            if (url == null) {
                return;
            }
            AccountType accountType    = securityManager.isLoggedIn() ? AccountType.OIDC : AccountType.ServiceAccount;
            ITokenManager tokenManager = null;

            switch (accountType){
                case OIDC:
                    tokenManager = tokenManagerFactory.Create(TokenManagerFactory.TokenManagerType.OIDC);
                    break;
                case ServiceAccount:
                    tokenManager = tokenManagerFactory.Create(TokenManagerFactory.TokenManagerType.ServiceAccount);
                    break;
                default:
                    tokenManager = tokenManagerFactory.Create(TokenManagerFactory.TokenManagerType.ServiceAccount);
                    break;
            }

            dataUpdateProcessor.process(httpFactory.create(tokenManager).GET(url));
            clearDataIfTruncateEventExist();

        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }
    }

    private long getFromDate() {
        return session.getLong(Constants.KEY_DATA_UPDATE_SET_FROM_DATE);
    }

    private void setFromDate(long fromDate) {
        session.setLong(Constants.KEY_DATA_UPDATE_SET_FROM_DATE, fromDate);
    }

    public String getUrl() {

        long latestDataUpdateId       = session.getLong(Constants.KEY_DATA_UPDATE_LAST_EVENT_ID);
        long latestDataUpdateIdFromDB = dataUpdateDataStore.getLatestDataUpdate();
        Map<String, Object>params     = new HashMap<>();

        if(latestDataUpdateId < latestDataUpdateIdFromDB ){
            latestDataUpdateId = latestDataUpdateIdFromDB;
            session.setLong(Constants.KEY_DATA_UPDATE_LAST_EVENT_ID, latestDataUpdateId);
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
            long lastWipeEventId = session.getLong(Constants.KEY_DATA_UPDATE_LAST_WIPE_EVENT_ID);
            dataUpdate           = dataUpdateDataStore.getTruncateDataUpdate();

            if (dataUpdate != null) {
                if (lastWipeEventId == 0 || lastWipeEventId < dataUpdate.getId()) {

                    Log.d(Constants.LOG_TAG, "DataUpdatePoller.clearDataIfTruncateEventExist: doing a wipe DB ...");
                    // reset state ...
                    session.setLong(Constants.KEY_DATA_UPDATE_LAST_EVENT_ID, 0);
                    session.setLong(Constants.KEY_DATA_UPDATE_SET_FROM_DATE, 0);
                    session.setLong(Constants.KEY_DATA_UPDATE_LAST_WIPE_EVENT_ID, dataUpdate.getId());
                    // check login state ...
                    if(securityManager.isLoggedIn()){
                        securityManager.logout();
                    }
                    dataUpdateDataStore.clearDataLocal();
                    // communicate the event
                    Intent intent = new Intent(Constants.WIPE_DATE_EVENT);
                    LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);
                    return;
                }
                dataUpdateDataStore.deleteDataUpdate(dataUpdate);
            }
        } while (dataUpdate != null);
    }

}