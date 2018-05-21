package org.openstack.android.summit.common.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class DataUpdatesService extends JobIntentService {

    public static final int JOB_ID  = 3000;
    @Inject
    IDataUpdatePoller dataUpdatePoller;

    @Inject
    IReachability reachability;

    public static Intent newIntent(Context context) {
        return new Intent(context, DataUpdatesService.class);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        ((OpenStackSummitApplication) getApplication()).getApplicationComponent().inject(this);
    }

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, DataUpdatesService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(Intent intent) {
        if (!reachability.isNetworkingAvailable(this)) {
            return;
        }
        // if we are on data ingestion running , skip it this
        if(SummitDataIngestionService.isRunning()) return;
        // normal flow ...
        dataUpdatePoller.pollServer();
        RealmFactory.closeSession();
    }

    public static void setServiceAlarm(Context context, boolean isOn) {

        Intent i                  = DataUpdatesService.newIntent(context);
        PendingIntent pi          = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            long interval = TimeUnit.MILLISECONDS.convert(context.getResources().getInteger(R.integer.data_updates_service_interval), TimeUnit.SECONDS);
            Log.d(Constants.LOG_TAG, "Starting Service DataUpdatesService");
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 100, interval, pi);
            return;
        }
        Log.d(Constants.LOG_TAG, "Stopping Service DataUpdatesService");
        alarmManager.cancel(pi);
        pi.cancel();
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = DataUpdatesService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    private static volatile HandlerThread handlerThread;
    private static volatile Handler serviceHandler;
    private static volatile Runnable runnableCode;

    public static void start(Context ctx){
        if(handlerThread != null && handlerThread.isAlive()) return;
        // An Android handler thread internally operates on a looper.
        handlerThread = new HandlerThread("DataUpdatesService.HandlerThread");
        handlerThread.start();
        // An Android service handler is a handler running on a specific background thread.
        serviceHandler = new Handler(DataUpdatesService.handlerThread.getLooper());
        long interval  = TimeUnit.MILLISECONDS.convert(ctx.getResources().getInteger(R.integer.user_actions_post_process_service_interval), TimeUnit.SECONDS);
        runnableCode   = new Runnable() {
            @Override
            public void run() {
                if(serviceHandler == null) return;

                Log.i(Constants.LOG_TAG, String.format("Calling service DataUpdatesService intent from thread %s", Thread.currentThread().getName()));

                DataUpdatesService.enqueueWork(ctx, DataUpdatesService.newIntent(ctx));
                serviceHandler.postDelayed(this, interval);
            }
        };
        serviceHandler.post(runnableCode);
    }

    public static boolean isRunning(){
        HandlerThread localHandlerThread = handlerThread;
        return  localHandlerThread != null && localHandlerThread.isAlive();
    }

    public static void stop(){
        HandlerThread localHandlerThread = handlerThread;
        Handler localServiceHandler      = serviceHandler;
        Runnable localRunnableCode       = runnableCode;
        if(localHandlerThread != null){
            Log.i(Constants.LOG_TAG, String.format("Stopping DataUpdatesService from thread %s", Thread.currentThread().getName()));
            if(localServiceHandler != null && localRunnableCode != null ) {
                localServiceHandler.removeCallbacks(localRunnableCode);
                serviceHandler = null;
                runnableCode   = null;
            }
            localHandlerThread.quit();
            handlerThread = null;
        }
    }
}
