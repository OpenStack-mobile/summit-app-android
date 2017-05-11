package org.openstack.android.summit.common.data_access.data_polling;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api.ISummitEntityEventsApi;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.BaseRemoteDataStore;
import org.openstack.android.summit.common.data_access.repositories.IDataUpdateDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Claudio Redi on 2/5/2016.
 */
public class DataUpdatePoller extends BaseRemoteDataStore implements IDataUpdatePoller {

    private ISecurityManager securityManager;
    private IDataUpdateProcessor dataUpdateProcessor;
    private IDataUpdateDataStore dataUpdateDataStore;
    private ISummitDataStore summitDataStore;
    private ISession session;
    private Retrofit restClientUserProfile;
    private Retrofit restClientServiceProfile;
    private ISummitSelector summitSelector;
    private Summit currentSummit = null;

    private static final int EntityEventUpdatesPageSize = 50;

    @Inject
    public DataUpdatePoller
    (
            ISecurityManager securityManager,
            IDataUpdateProcessor dataUpdateProcessor,
            IDataUpdateDataStore dataUpdateDataStore,
            ISummitDataStore summitDataStore,
            ISession session,
            @Named("MemberProfile") Retrofit restClientUserProfile,
            @Named("ServiceProfile") Retrofit restClientServiceProfile,
            ISummitSelector summitSelector
    )
    {

        this.securityManager          = securityManager;
        this.dataUpdateProcessor      = dataUpdateProcessor;
        this.dataUpdateDataStore      = dataUpdateDataStore;
        this.summitDataStore          = summitDataStore;
        this.session                  = session;
        this.restClientUserProfile    = restClientUserProfile;
        this.restClientServiceProfile = restClientServiceProfile;
        this.summitSelector           = summitSelector;
    }

    @Override
    public void pollServer() {

        try {

            int summitId = summitSelector.getCurrentSummitId();
            if(summitId <= 0) return;
            currentSummit = summitDataStore.getById(summitId);

            if(currentSummit == null) return;

            Log.d(Constants.LOG_TAG, "Polling server for data updates");

            Retrofit restClient     = securityManager.isLoggedIn() ?
                    this.restClientUserProfile :
                    this.restClientServiceProfile;

            Call<ResponseBody> call = getCall(restClient.create(ISummitEntityEventsApi.class));

            if (call == null) {
                return;
            }

            Response<ResponseBody> response = call.execute();

            if(!response.isSuccessful()) return;
            RealmFactory.transaction(session -> {
                dataUpdateProcessor.process(response.body().string());
                clearDataIfTruncateEventExist();
                return Void.getInstance();
            });

        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }
    }

    private Call<ResponseBody> getCall(ISummitEntityEventsApi api) {

        long latestDataUpdateId = dataUpdateDataStore.getLatestDataUpdate();

        if (latestDataUpdateId > 0){
            Log.i(Constants.LOG_TAG, String.format("DataUpdatePoller - calling api from id %d",latestDataUpdateId));
            return api.get(summitSelector.getCurrentSummitId(), null, latestDataUpdateId, EntityEventUpdatesPageSize);
        }

        long fromDate = 0;
        if (currentSummit != null) {
            fromDate = currentSummit.getInitialDataLoadDate().getTime() / 1000L;
        }

        if (fromDate > 0) {
            Log.d(Constants.LOG_TAG, String.format("DataUpdatePoller - calling api from date %d ",fromDate));
            return api.get(summitSelector.getCurrentSummitId(), fromDate, null, EntityEventUpdatesPageSize);
        }

        return null;
    }

    private void clearDataIfTruncateEventExist() {
        DataUpdate dataUpdate = null;
        do {
            long lastWipeEventId = session.getLong(Constants.KEY_DATA_UPDATE_LAST_WIPE_EVENT_ID);
            dataUpdate           = dataUpdateDataStore.getTruncateDataUpdate();

            if (dataUpdate != null) {

                Log.i(Constants.LOG_TAG, String.format("DataUpdatePoller.clearDataIfTruncateEventExist: lastWipeEventId %d - dataUpdate.getId() %d", lastWipeEventId, dataUpdate.getId()));
                if (lastWipeEventId == 0 || lastWipeEventId < dataUpdate.getId()) {

                    session.setLong(Constants.KEY_DATA_UPDATE_LAST_WIPE_EVENT_ID, dataUpdate.getId());
                    Log.i(Constants.LOG_TAG, "DataUpdatePoller.clearDataIfTruncateEventExist: doing a wipe DB ...");
                    // check login state ...
                    if(securityManager.isLoggedIn()){
                        securityManager.logout(false);
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