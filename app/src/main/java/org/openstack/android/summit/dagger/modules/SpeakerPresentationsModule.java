package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.data_access.repositories.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.ScheduleItemViewBuilder;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.speaker_presentations.ISpeakerPresentationsWireframe;
import org.openstack.android.summit.modules.speaker_presentations.SpeakerPresentationsWireframe;
import org.openstack.android.summit.modules.speaker_presentations.business_logic.ISpeakerPresentationsInteractor;
import org.openstack.android.summit.modules.speaker_presentations.business_logic.SpeakerPresentationsInteractor;
import org.openstack.android.summit.modules.speaker_presentations.user_interface.ISpeakerPresentationsPresenter;
import org.openstack.android.summit.modules.speaker_presentations.user_interface.SpeakerPresentationsFragment;
import org.openstack.android.summit.modules.speaker_presentations.user_interface.SpeakerPresentationsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
@Module
public class SpeakerPresentationsModule {
    @Provides
    SpeakerPresentationsFragment providesSpeakerPresentationsFragment() {
        return new SpeakerPresentationsFragment();
    }

    @Provides
    ISpeakerPresentationsInteractor providesSpeakerPresentationsInteractor(ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager, IPushNotificationsManager pushNotificationsManager, ISession session, ISummitSelector summitSelector) {
        return new SpeakerPresentationsInteractor(summitEventDataStore, summitDataStore, summitAttendeeDataStore, dtoAssembler, securityManager, pushNotificationsManager, session, summitSelector);
    }

    @Provides
    ISpeakerPresentationsWireframe providesSpeakerPresentationsWireframe(IEventDetailWireframe eventDetailWireframe, INavigationParametersStore navigationParametersStore) {
        return new SpeakerPresentationsWireframe(eventDetailWireframe, navigationParametersStore);
    }

    @Provides
    ISpeakerPresentationsPresenter providesSpeakerPresentationsPresenter(ISpeakerPresentationsInteractor interactor, ISpeakerPresentationsWireframe wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleFilter scheduleFilter) {
        return new SpeakerPresentationsPresenter(interactor, wireframe, scheduleablePresenter, new ScheduleItemViewBuilder(), scheduleFilter);
    }
}
