package org.openstack.android.summit.dagger.modules;

import android.app.Activity;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.data_access.IPresentationSpeakerDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.dagger.PerActivity;
import org.openstack.android.summit.modules.events.IEventsWireframe;
import org.openstack.android.summit.modules.main_activity.IMainActivityWireframe;
import org.openstack.android.summit.modules.main_activity.MainActivityWireframe;
import org.openstack.android.summit.modules.main_activity.business_logic.IMainActivityInteractor;
import org.openstack.android.summit.modules.main_activity.business_logic.MainActivityInteractor;
import org.openstack.android.summit.modules.main_activity.user_interface.IMainActivityPresenter;
import org.openstack.android.summit.modules.main_activity.user_interface.MainActivityPresenter;
import org.openstack.android.summit.modules.member_profile.IMemberProfileWireframe;
import org.openstack.android.summit.modules.search.ISearchWireframe;
import org.openstack.android.summit.modules.speakers_list.ISpeakerListWireframe;
import org.openstack.android.summit.modules.venues.IVenuesWireframe;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 11/3/2015.
 */
@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    /**
     * Expose the activity to dependents in the graph.
     */
    @Provides
    @PerActivity
    Activity activity() {
        return this.activity;
    }
}
