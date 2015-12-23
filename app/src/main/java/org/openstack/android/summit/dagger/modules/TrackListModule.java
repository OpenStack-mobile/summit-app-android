package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.modules.track_list.user_interface.TrackListFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 12/22/2015.
 */
@Module
public class TrackListModule {
    @Provides
    TrackListFragment providesTrackListFragment() {
        return new TrackListFragment();
    }
}
