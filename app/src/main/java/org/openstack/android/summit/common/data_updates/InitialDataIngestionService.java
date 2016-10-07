package org.openstack.android.summit.common.data_updates;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.github.kevinsawicki.http.HttpRequest;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.api_endpoints.ApiEndpointBuilder;
import org.openstack.android.summit.common.data_access.deserialization.ISummitDeserializer;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.network.HttpTaskConfig;
import org.openstack.android.summit.common.network.IHttpFactory;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.IOIDCConfigurationManager;
import org.openstack.android.summit.common.security.ITokenManager;
import org.openstack.android.summit.common.security.ITokenManagerFactory;
import org.openstack.android.summit.common.security.TokenManagerFactory;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;
import org.openstack.android.summit.modules.general_schedule.business_logic.IGeneralScheduleInteractor;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by sebastian on 10/5/2016.
 */

public class InitialDataIngestionService extends IntentService {

    public static final String PENDING_RESULT = "pending_result";
    public static final int RESULT_CODE_OK    = 0xFF01;
    public static final int RESULT_CODE_ERROR = 0xFF02;

    public static boolean isRunning = false;

    @Inject
    IReachability reachability;

    @Inject
    IGeneralScheduleInteractor interactor;

    @Inject
    ITokenManagerFactory tokenManagerFactory;

    @Inject
    IHttpFactory httpFactory;

    @Inject
    ISummitDeserializer deserializer;

    @Inject
    IOIDCConfigurationManager ioidcConfigurationManager;

    public static Intent newIntent(Context context) {
        return new Intent(context, InitialDataIngestionService.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ((OpenStackSummitApplication) getApplication()).getApplicationComponent().inject(this);
        return super.onStartCommand(intent, flags, startId);
    }

    public InitialDataIngestionService() {
        super("InitialDataIngestionService");
        this.setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent result       = new Intent();
        PendingIntent reply = intent.getParcelableExtra(PENDING_RESULT);

        try {

            isRunning  = true;

            if (!reachability.isNetworkingAvailable(this)) {
                isRunning = false;
                reply.send(this, RESULT_CODE_ERROR, result);
                return;
            }

            if(interactor.isDataLoaded())
            {
                Log.d(Constants.LOG_TAG, "InitialDataIngestionService.onHandleIntent: data already loaded !!!");
                isRunning = false;
                reply.send(this, RESULT_CODE_OK, result);
                return;
            }

            HttpTaskConfig config        = new HttpTaskConfig();
            Map<String,Object> params    = new HashMap<>();
            String resourceServerBaseUrl = ioidcConfigurationManager.getResourceServerBaseUrl();
            ITokenManager tokenManager   = tokenManagerFactory.Create(TokenManagerFactory.TokenManagerType.ServiceAccount);

            params.put(ApiEndpointBuilder.ExpandParam,"locations,sponsors,summit_types,event_types,presentation_categories,schedule" );

            config.setUrl(ApiEndpointBuilder.getInstance().buildEndpoint(resourceServerBaseUrl, "current", ApiEndpointBuilder.EndpointType.GetSummit, params).toString());
            config.setMethod(HttpRequest.METHOD_GET);
            config.setContentType(null);
            config.setContent(null);
            config.setDelegate(null);
            config.setHttp(httpFactory.create(tokenManager));
            Log.d(Constants.LOG_TAG, "InitialDataIngestionService.onHandleIntent: getting summit data ...");
            final String body = config.getHttp().GET(config.getUrl());

            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {

                    Log.d(Constants.LOG_TAG, "InitialDataIngestionService.onHandleIntent: deserializing summit data ...");
                    Summit summit = deserializer.deserialize(body);
                    session.copyToRealmOrUpdate(summit);
                    return Void.getInstance();
                }
            });

            Log.d(Constants.LOG_TAG, "InitialDataIngestionService.onHandleIntent: summit data loaded !!!");
            reply.send(this, RESULT_CODE_OK, result);
        }
        catch (Exception ex) {
            Log.e(Constants.LOG_TAG, ex.getMessage());
            Crashlytics.logException(ex);
            try
            {
                isRunning = false;
                reply.send(this, RESULT_CODE_ERROR, result);
            }
            catch (PendingIntent.CanceledException ex2){
                Log.e(Constants.LOG_TAG, ex2.getMessage());
                Crashlytics.logException(ex2);
            }
        }
        finally {
            isRunning = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }
}
