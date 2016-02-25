package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.NavigationParametersStore;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.network.Reachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.modules.event_detail.EventDetailWireframe;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.event_detail.business_logic.EventDetailInteractor;
import org.openstack.android.summit.modules.event_detail.business_logic.IEventDetailInteractor;
import org.openstack.android.summit.modules.event_detail.user_interface.EventDetailFragment;
import org.openstack.android.summit.modules.event_detail.user_interface.EventDetailPresenter;
import org.openstack.android.summit.modules.event_detail.user_interface.IEventDetailPresenter;
import org.openstack.android.summit.modules.feedback_edit.IFeedbackEditWireframe;
import org.openstack.android.summit.modules.member_profile.IMemberProfileWireframe;
import org.openstack.android.summit.modules.venue_detail.IVenueDetailWireframe;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
@Module
public class EventDetailModule {
    @Provides
    EventDetailFragment providesEventDetailFragment() {
        return new EventDetailFragment();
    }

    @Provides
    IEventDetailPresenter providesEventDetailPresenter(IEventDetailInteractor eventDetailInteractor, IEventDetailWireframe eventDetailWireframe, IScheduleablePresenter scheduleablePresenter) {
        return new EventDetailPresenter(eventDetailInteractor, eventDetailWireframe, scheduleablePresenter);
    }

    @Provides
    IEventDetailInteractor providesEventDetailInteractor(ISummitEventDataStore summitEventDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager, IDataUpdatePoller dataUpdatePoller) {
        return new EventDetailInteractor(summitEventDataStore, summitAttendeeDataStore, new Reachability(), dtoAssembler, securityManager, dataUpdatePoller);
    }

    @Provides
    IEventDetailWireframe providesEventDetailWireframe(IMemberProfileWireframe memberProfileWireframe, IFeedbackEditWireframe feedbackEditWireframe,INavigationParametersStore navigationParametersStore, IVenueDetailWireframe venueDetailWireframe) {
        return new EventDetailWireframe(memberProfileWireframe, feedbackEditWireframe, navigationParametersStore, venueDetailWireframe);
    }
}
