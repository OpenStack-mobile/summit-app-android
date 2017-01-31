package org.openstack.android.summit.common.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
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

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by sebastian on 10/5/2016.
 */

public class SummitDataIngestionService extends IntentService {

    public static final String ACTION         = "org.openstack.android.summit.common.services.SummitDataIngestionService.Action";
    public static final int RESULT_CODE_OK    = 0xFF01;
    public static final int RESULT_CODE_ERROR = 0xFF02;
    private static boolean isRunning          = false;

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
    public int onStartCommand(Intent intent, int flags, int startId) {
        ((OpenStackSummitApplication) getApplication()).getApplicationComponent().inject(this);
        return super.onStartCommand(intent, flags, startId);
    }

    public SummitDataIngestionService() {
        super("SummitDataIngestionService");
        this.setIntentRedelivery(true);
    }

    private void sendResult(Intent result , int code){
        result.putExtra("res", code);
        LocalBroadcastManager.getInstance(this).sendBroadcast(result);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Intent result  = new Intent(ACTION);

        try {

            setRunning(true);

            if (!reachability.isNetworkingAvailable(this)) {
                setRunning(false);
                sendResult(result, RESULT_CODE_ERROR);
                return;
            }

            Log.d(Constants.LOG_TAG, "SummitDataIngestionService.onHandleIntent: getting summit data ...");

            Call<ResponseBody> call = restClient.create(ISummitApi.class).getSummit(summitSelector.getCurrentSummitId(), "schedule");

            final retrofit2.Response<ResponseBody> response = call.execute();

            if (!response.isSuccessful())
                throw new Exception(String.format("SummitDataIngestionService: invalid http code %d", response.code()));

            final String body = response.body().string();

            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {

                    Log.d(Constants.LOG_TAG, "SummitDataIngestionService.onHandleIntent: deserializing summit data ...");
                    Summit summit = deserializer.deserialize(body);
                    session.copyToRealmOrUpdate(summit);
                    return Void.getInstance();
                }
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
