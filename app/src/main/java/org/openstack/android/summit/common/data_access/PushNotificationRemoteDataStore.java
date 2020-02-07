package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.api.INotificationsApi;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.entities.notifications.PushNotification;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.utils.RealmFactory;
import java.util.List;
import javax.inject.Named;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class PushNotificationRemoteDataStore
        extends BaseRemoteDataStore implements IPushNotificationRemoteDataStore {

    private IDeserializer deserializer;
    private Retrofit restClientUserProfile;
    private Retrofit restClientServiceProfile;
    private ISummitSelector summitSelector;
    private ISecurityManager securityManager;

    public PushNotificationRemoteDataStore
    (
            ISecurityManager securityManager,
            IDeserializer deserializer,
            @Named("MemberProfile") Retrofit restClientUserProfile,
            @Named("ServiceProfile") Retrofit restClientServiceProfile,
            ISummitSelector summitSelector
    ) {
        this.securityManager          = securityManager;
        this.deserializer             = deserializer;
        this.deserializer.setSecurityManager(this.securityManager);
        this.restClientUserProfile    = restClientUserProfile;
        this.restClientServiceProfile = restClientServiceProfile;
        this.summitSelector           = summitSelector;
    }

    /**
     *
     * @param searchTerm
     * @param page
     * @param objectsPerPage
     * @return
     */
    @Override
    public Observable<List<PushNotification>> get(String searchTerm, int page, int objectsPerPage) {

        Retrofit restClient = securityManager.isLoggedIn() ?
                this.restClientUserProfile :
                this.restClientServiceProfile;

        String filter = null;

        if(searchTerm != null && !searchTerm.isEmpty()){
            filter = "message=@"+searchTerm;
        }

        return restClient.create(INotificationsApi.class)
                .getSent(summitSelector.getCurrentSummitId(),
                        page,
                        objectsPerPage,
                        filter,
                        "-sent_date")
                .subscribeOn(Schedulers.io())
                .map( response -> {
                    if(!response.isSuccessful()){
                        throw new Exception
                                (
                                        String.format
                                                (
                                                        "getFeedback: http error %d",
                                                        response.code()
                                                )
                                );
                    }
                    return RealmFactory.transaction(session ->
                            deserializer.deserializePage(response.body().string(), PushNotification.class)
                    );
                }).doOnTerminate(RealmFactory::closeSession);

    }
}
