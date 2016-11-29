package org.openstack.android.summit.common.data_access.data_polling;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api.ISummitEntityEventsApi;
import org.openstack.android.summit.common.api.SummitSelector;
import org.openstack.android.summit.common.data_access.BaseRemoteDataStore;
import org.openstack.android.summit.common.data_access.IDataUpdateDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.security.ISecurityManager;

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
            @Named("ServiceProfile") Retrofit restClientServiceProfile
    )
    {

        this.securityManager          = securityManager;
        this.dataUpdateProcessor      = dataUpdateProcessor;
        this.dataUpdateDataStore      = dataUpdateDataStore;
        this.summitDataStore          = summitDataStore;
        this.session                  = session;
        this.restClientUserProfile    = restClientUserProfile;
        this.restClientServiceProfile = restClientServiceProfile;
    }

    @Override
    public void pollServer() {

        try {

            Log.d(Constants.LOG_TAG, "Polling server for data updates");

            Retrofit restClient     = securityManager.isLoggedIn() ?
                    this.restClientUserProfile :
                    this.restClientServiceProfile;

            Call<ResponseBody> call = getCall(restClient.create(ISummitEntityEventsApi.class));

            if (call == null) {
                return;
            }

            if(summitDataStore.getActive() == null){
                return;
            }

            Response<ResponseBody> response = call.execute();

            if(!response.isSuccessful()) return;

            dataUpdateProcessor.process(response.body().string());
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

    private Call<ResponseBody> getCall(ISummitEntityEventsApi api) {

        long latestDataUpdateId       = session.getLong(Constants.KEY_DATA_UPDATE_LAST_EVENT_ID);
        long latestDataUpdateIdFromDB = dataUpdateDataStore.getLatestDataUpdate();

        if(latestDataUpdateId < latestDataUpdateIdFromDB ){
            latestDataUpdateId = latestDataUpdateIdFromDB;
            session.setLong(Constants.KEY_DATA_UPDATE_LAST_EVENT_ID, latestDataUpdateId);
        }

        if (latestDataUpdateId > 0){
            return api.get(SummitSelector.getCurrentSummitId(), null, latestDataUpdateId, EntityEventUpdatesPageSize);
        }

        long fromDate = getFromDate();
        if (fromDate == 0) {
            Summit summit = summitDataStore.getActive();
            if (summit != null) {
                fromDate = summit.getInitialDataLoadDate().getTime() / 1000L;
                setFromDate(fromDate);
            }
        }

        if (fromDate != 0) {
            return api.get(SummitSelector.getCurrentSummitId(), fromDate, null, EntityEventUpdatesPageSize);
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
                    clearState(session);
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

    public static void clearState(ISession session){
        if(session == null ) return;
        session.setLong(Constants.KEY_DATA_UPDATE_LAST_EVENT_ID, 0);
        session.setLong(Constants.KEY_DATA_UPDATE_SET_FROM_DATE, 0);
    }

}