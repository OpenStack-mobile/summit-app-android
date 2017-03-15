package org.openstack.android.summit.common.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.api.ISummitApi;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.deserialization.ISummitDeserializer;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.utils.RealmFactory;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by smarcet on 12/7/16.
 */

public class SummitsListIngestionService extends IntentService {

    public static final String ACTION                                   = "org.openstack.android.summit.common.services.SummitsListIngestionService.Action";
    public static final int RESULT_CODE_OK                              = 0xFF03;
    public static final int RESULT_CODE_OK_INITIAL_LOADING              = 0xFF04;
    public static final int RESULT_CODE_OK_NEW_SUMMIT_AVAILABLE_LOADING = 0xFF05;
    public static final int RESULT_CODE_ERROR                           = 0xFF06;
    private static boolean isRunning                                    = false;

    public static boolean isRunning(){
        synchronized (SummitsListIngestionService.class){
            return isRunning;
        }
    }

    private static void setRunning(boolean state){
        synchronized (SummitsListIngestionService.class){
            isRunning = state;
        }
    }

    @Inject
    IReachability reachability;

    @Inject
    ISummitDeserializer deserializer;

    @Inject
    ISummitSelector summitSelector;

    @Inject
    ISummitDataStore summitDataStore;

    @Inject
    @Named("ServiceProfile")
    Retrofit restClient;

    public static Intent newIntent(Context context) {
        return new Intent(context, SummitsListIngestionService.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ((OpenStackSummitApplication) getApplication()).getApplicationComponent().inject(this);
        return super.onStartCommand(intent, flags, startId);
    }

    public SummitsListIngestionService() {
        super("SummitsListIngestionService");
        this.setIntentRedelivery(true);
    }

    private void sendResult(Intent result , int code){
        result.putExtra("res", code);
        LocalBroadcastManager.getInstance(this).sendBroadcast(result);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Intent result = new Intent(ACTION);
        try {

            setRunning(true);

            if (!reachability.isNetworkingAvailable(this)) {
                setRunning(false);
                sendResult(result, RESULT_CODE_ERROR);
                return;
            }

            Log.d(Constants.LOG_TAG, "SummitsListIngestionService.onHandleIntent: getting list of summits ...");

            Call<ResponseBody> call = restClient.create(ISummitApi.class).getSummits();

            final retrofit2.Response<ResponseBody> response = call.execute();

            if (!response.isSuccessful())
                throw new Exception(String.format("SummitsListIngestionService: invalid http code %d", response.code()));

            final String body           = response.body().string();
            final JSONObject jsonObject = new JSONObject(body);


            //check if current summit id exists on our local ...
            Summit currentSummit = RealmFactory.getSession().where(Summit.class).equalTo("id", summitSelector.getCurrentSummitId()).findFirst();
            if(currentSummit == null || !currentSummit.isScheduleLoaded()){
                summitSelector.clearCurrentSummit();
            }

            int res = RealmFactory.transaction(session -> {
                Boolean mustReadSummitData = false;
                Log.d(Constants.LOG_TAG, "SummitsListIngestionService.onHandleIntent: deserializing summit list data ...");
                JSONArray summitsList = jsonObject.getJSONArray("data");

                for (int i = 0; i < summitsList.length(); i++) {
                    JSONObject summitJson = summitsList.getJSONObject(i);
                    deserializer.setMode(ISummitDeserializer.SummitDeserializerMode.Header);
                    Summit summit = deserializer.deserialize(summitJson.toString());
                    session.copyToRealmOrUpdate(summit);
                }
                int currentSummitId = summitSelector.getCurrentSummitId();
                // get latest summit
                Summit latestSummit = summitDataStore.getLatest();

                int res1            = RESULT_CODE_OK;

                Log.i(Constants.LOG_TAG, String.format("SummitsListIngestionService : currentSummitId %d", currentSummitId));
                if(latestSummit != null ){
                    Log.i(Constants.LOG_TAG, String.format("SummitsListIngestionService : latestSummit.getId() %d", latestSummit.getId()));
                }

                if(latestSummit !=null && ( latestSummit.getId() != currentSummitId || !latestSummit.isScheduleLoaded() )) {
                    res1 = RESULT_CODE_OK_INITIAL_LOADING;
                    if(currentSummitId > 0 && latestSummit.getId() > currentSummitId){
                        // we have a new summit available
                        return RESULT_CODE_OK_NEW_SUMMIT_AVAILABLE_LOADING;
                    }
                    summitSelector.setCurrentSummitId(latestSummit.getId());
                }

                return res1;
            });
            Log.d(Constants.LOG_TAG, "SummitsListIngestionService.onHandleIntent: summit list data loaded !!!");
            sendResult(result, res);
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
