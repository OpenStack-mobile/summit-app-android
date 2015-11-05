package org.openstack.android.openstacksummit.dagger.modules;

import android.app.Activity;

import org.openstack.android.openstacksummit.dagger.PerActivity;
import org.openstack.android.openstacksummit.modules.general_schedule.user_interface.GeneralScheduleFragment;

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
