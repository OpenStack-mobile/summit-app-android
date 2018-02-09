package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.business_logic.IProcessableUserActionManager;
import org.openstack.android.summit.common.business_logic.ProcessableUserActionManager;
import org.openstack.android.summit.common.data_access.repositories.IMyFavoriteProcessableUserActionDataStore;
import org.openstack.android.summit.common.data_access.repositories.IMyFeedbackProcessableUserActionDataStore;
import org.openstack.android.summit.common.data_access.repositories.IMyRSVPProcessableUserActionDataStore;
import org.openstack.android.summit.common.data_access.repositories.IMyScheduleProcessableUserActionDataStore;
import org.openstack.android.summit.common.security.IPrincipalIdentity;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by smarcet on 2/8/18.
 */
@Module
public class BusinessLogicModule {

    @Singleton
    @Provides
    public IProcessableUserActionManager providesProcessableUserActionManager
    (
            IMyScheduleProcessableUserActionDataStore myScheduleProcessableUserActionDataStore,
            IMyFavoriteProcessableUserActionDataStore myFavoriteProcessableUserActionDataStore,
            IMyFeedbackProcessableUserActionDataStore myFeedbackProcessableUserActionDataStore,
            IMyRSVPProcessableUserActionDataStore myRSVPProcessableUserActionDataStore,
            @Named("MemberProfile") Retrofit restClientRxJava,
            IPrincipalIdentity principalIdentity
    )
    {
        return new ProcessableUserActionManager
        (
            myScheduleProcessableUserActionDataStore,
            myFavoriteProcessableUserActionDataStore,
            myFeedbackProcessableUserActionDataStore,
            myRSVPProcessableUserActionDataStore,
            restClientRxJava,
            principalIdentity
        );
    }
}
