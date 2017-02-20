package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.api.ISummitEventsApi;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public class SummitAttendeeRemoteDataStore extends BaseRemoteDataStore implements ISummitAttendeeRemoteDataStore {

    private Retrofit restClient;
    private ISummitEventsApi summitEventsApi;
    private ISummitSelector summitSelector;

    @Inject
    public SummitAttendeeRemoteDataStore
    (
        @Named("MemberProfile") Retrofit restClient,
        ISummitSelector summitSelector
    )
    {
        this.restClient      = restClient;
        this.summitEventsApi = restClient.create(ISummitEventsApi.class);
        this.summitSelector  = summitSelector;
    }

    @Override
    public void addEventToSchedule
    (
        final SummitAttendee summitAttendee,
        final SummitEvent summitEvent,
        final IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener
    )
    {
        Call<ResponseBody> call = summitEventsApi.addToMySchedule(summitSelector.getCurrentSummitId(), summitEvent.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try
                {

                    if(!response.isSuccessful()) {
                        switch (response.code()) {
                            case 412:
                                dataStoreOperationListener.onError
                                (
                                    "Event already belongs to user schedule"
                                );
                                return;
                            case 404:
                                dataStoreOperationListener.onError
                                (
                                    "Event not found"
                                );
                                return;

                        }
                    }
                    dataStoreOperationListener.onSucceedWithSingleData(summitAttendee);
                }
                catch(Exception ex){
                    Crashlytics.logException(ex);
                    Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
                    dataStoreOperationListener.onError(ex.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String friendlyError = Constants.GENERIC_ERROR_MSG;
                dataStoreOperationListener.onError(friendlyError);
            }
        });
    }

    @Override
    public void removeEventFromSchedule
    (
        final SummitAttendee summitAttendee,
        final SummitEvent summitEvent,
        final IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener
    )
    {

        Call<ResponseBody> call = summitEventsApi.removeFromMySchedule(summitSelector.getCurrentSummitId(), summitEvent.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try
                {
                    if(!response.isSuccessful()) {
                        switch (response.code()) {
                            case 412:
                                dataStoreOperationListener.onError
                                (
                                    "Event does not belongs to user schedule"
                                );
                                return;
                            case 404:
                                dataStoreOperationListener.onError
                                 (
                                    "Event not found"
                                 );
                                return;

                        }
                    }
                    dataStoreOperationListener.onSucceedWithSingleData(summitAttendee);
                }
                catch(Exception ex){
                    Crashlytics.logException(ex);
                    Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
                    dataStoreOperationListener.onError(ex.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String friendlyError = Constants.GENERIC_ERROR_MSG;
                dataStoreOperationListener.onError(friendlyError);
            }
        });
    }

}
