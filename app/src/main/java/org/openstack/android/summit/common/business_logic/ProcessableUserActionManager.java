package org.openstack.android.summit.common.business_logic;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.api.IMembersApi;
import org.openstack.android.summit.common.api.ISummitEventsApi;
import org.openstack.android.summit.common.api.SummitEventFeedbackRequest;
import org.openstack.android.summit.common.data_access.repositories.IMyFavoriteProcessableUserActionDataStore;
import org.openstack.android.summit.common.data_access.repositories.IMyFeedbackProcessableUserActionDataStore;
import org.openstack.android.summit.common.data_access.repositories.IMyRSVPProcessableUserActionDataStore;
import org.openstack.android.summit.common.data_access.repositories.IMyScheduleProcessableUserActionDataStore;
import org.openstack.android.summit.common.entities.processable_user_actions.MyFavoriteProcessableUserAction;
import org.openstack.android.summit.common.entities.processable_user_actions.MyFeedbackProcessableUserAction;
import org.openstack.android.summit.common.entities.processable_user_actions.MyRSVPProcessableUserAction;
import org.openstack.android.summit.common.entities.processable_user_actions.MyScheduleProcessableUserAction;
import org.openstack.android.summit.common.security.IPrincipalIdentity;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import java.util.List;

import javax.inject.Named;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by smarcet on 2/8/18.
 */

final public class ProcessableUserActionManager implements IProcessableUserActionManager {

    private IMyScheduleProcessableUserActionDataStore myScheduleProcessableUserActionDataStore;
    private IMyFavoriteProcessableUserActionDataStore myFavoriteProcessableUserActionDataStore;
    private IMyFeedbackProcessableUserActionDataStore myFeedbackProcessableUserActionDataStore;
    private IMyRSVPProcessableUserActionDataStore     myRSVPProcessableUserActionDataStore;
    private IMembersApi memberApi;
    private ISummitEventsApi summitEventsApi;
    private IPrincipalIdentity principalIdentity;

    public ProcessableUserActionManager
    (
            IMyScheduleProcessableUserActionDataStore myScheduleProcessableUserActionDataStore,
            IMyFavoriteProcessableUserActionDataStore myFavoriteProcessableUserActionDataStore,
            IMyFeedbackProcessableUserActionDataStore myFeedbackProcessableUserActionDataStore,
            IMyRSVPProcessableUserActionDataStore myRSVPProcessableUserActionDataStore,
            @Named("MemberProfileRXJava2") Retrofit restClientRxJava,
            IPrincipalIdentity principalIdentity
    )
    {
        this.myScheduleProcessableUserActionDataStore = myScheduleProcessableUserActionDataStore;
        this.myFavoriteProcessableUserActionDataStore = myFavoriteProcessableUserActionDataStore;
        this.myFeedbackProcessableUserActionDataStore = myFeedbackProcessableUserActionDataStore;
        this.myRSVPProcessableUserActionDataStore     = myRSVPProcessableUserActionDataStore;
        this.memberApi                                = restClientRxJava.create(IMembersApi.class);
        this.summitEventsApi                          = restClientRxJava.create(ISummitEventsApi.class);
        this.principalIdentity                        = principalIdentity;
    }

    @Override
    public void processMyScheduleProcessableUserActions() {
        try {
            RealmFactory.transaction(session -> {
                List<MyScheduleProcessableUserAction> list = myScheduleProcessableUserActionDataStore.getAllUnProcessed(principalIdentity.getCurrentMemberId());
                for(MyScheduleProcessableUserAction action: list){
                    try{
                        switch(action.getType()){
                            case "Add":
                            {
                                Call<ResponseBody> call = memberApi.addToMySchedule(action.getEvent().getSummit().getId(), action.getEvent().getId());
                                final retrofit2.Response<ResponseBody> response = call.execute();
                                Log.i(Constants.LOG_TAG, String.format("call to addToMySchedule for event id %d returned http code %d",action.getEvent().getId(), response.code()));

                            }
                            break;
                            case "Remove":
                            {
                                Call<ResponseBody> call = memberApi.removeFromMySchedule(action.getEvent().getSummit().getId(), action.getEvent().getId());
                                final retrofit2.Response<ResponseBody> response = call.execute();
                                Log.i(Constants.LOG_TAG, String.format("call to removeFromMySchedule for event id %d returned http code %d",action.getEvent().getId(), response.code()));
                            }
                            break;
                        }
                        action.markAsProcessed();

                        session.insertOrUpdate(action);
                    }
                    catch (Exception ex){
                        Log.w(Constants.LOG_TAG, ex.getMessage(), ex);
                    }

                }
                return Void.getInstance();
            });
        }
        catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }
    }

    @Override
    public void processMyFavoritesProcessableUserActions() {
        try {
            RealmFactory.transaction(session -> {
                List<MyFavoriteProcessableUserAction> list = myFavoriteProcessableUserActionDataStore.getAllUnProcessed(principalIdentity.getCurrentMemberId());
                for(MyFavoriteProcessableUserAction action: list){
                    try{
                        switch(action.getType()){
                            case "Add":
                            {
                                Call<ResponseBody> call = memberApi.addToFavorites(action.getEvent().getSummit().getId(), action.getEvent().getId());
                                final retrofit2.Response<ResponseBody> response = call.execute();
                                Log.i(Constants.LOG_TAG, String.format("call to addToFavorites for event id %d returned http code %d",action.getEvent().getId(), response.code()));

                            }
                            break;
                            case "Remove":
                            {
                                Call<ResponseBody> call = memberApi.removeFromFavorites(action.getEvent().getSummit().getId(), action.getEvent().getId());
                                final retrofit2.Response<ResponseBody> response = call.execute();
                                Log.i(Constants.LOG_TAG, String.format("call to removeFromFavorites for event id %d returned http code %d",action.getEvent().getId(), response.code()));
                            }
                            break;
                        }
                        action.markAsProcessed();

                        session.insertOrUpdate(action);
                    }
                    catch (Exception ex){
                        Log.w(Constants.LOG_TAG, ex.getMessage(), ex);
                    }

                }
                return Void.getInstance();
            });
        }
        catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }
    }

    @Override
    public void processMyFeedbackProcessableUserActions() {
        try {
            RealmFactory.transaction(session -> {
                List<MyFeedbackProcessableUserAction> list = myFeedbackProcessableUserActionDataStore.getAllUnProcessed(principalIdentity.getCurrentMemberId());
                for(MyFeedbackProcessableUserAction action: list){
                    try{
                        switch(action.getType()){
                            case "Add":
                            {
                                Call<ResponseBody> call = summitEventsApi.postEventFeedback
                                        (
                                                action.getEvent().getSummit().getId(),
                                                action.getEvent().getId(),
                                                new SummitEventFeedbackRequest(action.getRate(), action.getReview())
                                        );
                                final retrofit2.Response<ResponseBody> response = call.execute();
                                Log.i(Constants.LOG_TAG, String.format("call to postEventFeedback for event id %d returned http code %d",action.getEvent().getId(), response.code()));

                            }
                            break;
                            case "Update":
                            {
                                Call<ResponseBody> call = summitEventsApi.updateEventFeedback
                                        (
                                                action.getEvent().getSummit().getId(),
                                                action.getEvent().getId(),
                                                new SummitEventFeedbackRequest(action.getRate(), action.getReview())
                                        );
                                final retrofit2.Response<ResponseBody> response = call.execute();
                                Log.i(Constants.LOG_TAG, String.format("call to updateEventFeedback for event id %d returned http code %d", action.getEvent().getId(), response.code()));
                            }
                            break;
                        }
                        action.markAsProcessed();

                        session.insertOrUpdate(action);
                    }
                    catch (Exception ex){
                        Log.w(Constants.LOG_TAG, ex.getMessage(), ex);
                    }

                }
                return Void.getInstance();
            });
        }
        catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }
    }

    @Override
    public void processMyRSVPProcessableUserActions() {
        try {
            RealmFactory.transaction(session -> {
                List<MyRSVPProcessableUserAction> list = myRSVPProcessableUserActionDataStore.getAllUnProcessed(principalIdentity.getCurrentMemberId());
                for(MyRSVPProcessableUserAction action: list){
                    try{
                        switch(action.getType()){
                            case "Add":
                            {
                            }
                            break;
                            case "Remove":
                            {
                                Call<ResponseBody> call = memberApi.deleteRSVP(action.getEvent().getSummit().getId(), action.getEvent().getId());
                                final retrofit2.Response<ResponseBody> response = call.execute();
                                Log.i(Constants.LOG_TAG, String.format("call to deleteRSVP for event id %d returned http code %d",action.getEvent().getId(), response.code()));
                            }
                            break;
                        }
                        action.markAsProcessed();

                        session.insertOrUpdate(action);
                    }
                    catch (Exception ex){
                        Log.w(Constants.LOG_TAG, ex.getMessage(), ex);
                    }

                }
                return Void.getInstance();
            });
        }
        catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }
    }
}
