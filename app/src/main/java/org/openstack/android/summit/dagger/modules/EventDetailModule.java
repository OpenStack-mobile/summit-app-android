package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.modules.event_detail.EventDetailWireframe;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
@Module
public class EventDetailModule {
    @Provides
    IEventDetailWireframe providesEventDetailWireframe() {
        return new EventDetailWireframe();
    }
}
