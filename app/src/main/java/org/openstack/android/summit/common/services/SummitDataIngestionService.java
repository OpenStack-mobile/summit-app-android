package org.openstack.android.summit.common.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.JobIntentService;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.api.ISummitApi;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.deserialization.ISummitDeserializer;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;
import org.openstack.android.summit.modules.general_schedule.business_logic.IGeneralScheduleInteractor;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by sebastian on 10/5/2016.
 */

public class SummitDataIngestionService extends JobIntentService {

    public static final String ACTION         = "org.openstack.android.summit.common.services.SummitDataIngestionService.Action";
    public static final int RESULT_CODE_OK    = 0xFF01;
    public static final int RESULT_CODE_ERROR = 0xFF02;
    private static boolean isRunning          = false;
    public static final int JOB_ID            = 2000;

    public static boolean isRunning(){
        synchronized (SummitDataIngestionService.class){
            return isRunning;
        }
    }

    private static void setRunning(boolean state){
        synchronized (SummitDataIngestionService.class){
            isRunning = state;
        }
    }

    @Inject
    IReachability reachability;

    @Inject
    IGeneralScheduleInteractor interactor;

    @Inject
    ISummitDeserializer deserializer;

    @Inject
    ISummitSelector summitSelector;

    @Inject
    @Named("ServiceProfile")
    Retrofit restClient;

    public static Intent newIntent(Context context) {
        return new Intent(context, SummitDataIngestionService.class);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        ((OpenStackSummitApplication) getApplication()).getApplicationComponent().inject(this);
    }

    private void sendResult(Intent result , int code){
        result.putExtra("res", code);
        LocalBroadcastManager.getInstance(this).sendBroadcast(result);
    }

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, SummitDataIngestionService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(Intent intent) {

        Intent result  = new Intent(ACTION);

        try {

            setRunning(true);

            if (!reachability.isNetworkingAvailable(this)) {
                setRunning(false);
                sendResult(result, RESULT_CODE_ERROR);
                return;
            }

            int currentSummitId  = summitSelector.getCurrentSummitId();
            Log.d(Constants.LOG_TAG, String.format("SummitDataIngestionService.onHandleIntent: currentSummitId %d", currentSummitId));
            Summit currentSummit = RealmFactory.getSession().where(Summit.class).equalTo("id", currentSummitId).findFirst();
            if(currentSummit != null && currentSummit.isScheduleLoaded()){
                Log.d(Constants.LOG_TAG, "SummitDataIngestionService.onHandleIntent: current summit data already loaded ! bypassing ...");
                setRunning(false);
                sendResult(result, RESULT_CODE_OK);
                return;
            }

            Log.d(Constants.LOG_TAG, "SummitDataIngestionService.onHandleIntent: getting summit data ...");

            Call<ResponseBody> call = restClient.create(ISummitApi.class).getSummit(currentSummitId, "schedule");

            final retrofit2.Response<ResponseBody> response = call.execute();

            if (!response.isSuccessful())
                throw new Exception(String.format("SummitDataIngestionService: invalid http code %d", response.code()));

            final String body = response.body().string();

            RealmFactory.transaction(session -> {

                Log.d(Constants.LOG_TAG, "SummitDataIngestionService.onHandleIntent: deserializing summit data ...");
                deserializer.setMode(ISummitDeserializer.SummitDeserializerMode.Complete);
                Summit summit = deserializer.deserialize(body);
                session.copyToRealmOrUpdate(summit);
                return Void.getInstance();
            });

            Log.d(Constants.LOG_TAG, "SummitDataIngestionService.onHandleIntent: summit data loaded !!!");
            sendResult(result, RESULT_CODE_OK);

        } catch (Exception ex) {
            setRunning(false);
            sendResult(result, RESULT_CODE_ERROR);
            Crashlytics.logException(ex);
        } finally {
            setRunning(false);
            RealmFactory.closeSession();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setRunning(false);
    }
}
