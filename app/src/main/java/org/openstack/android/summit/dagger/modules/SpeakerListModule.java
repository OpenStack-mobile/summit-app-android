package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.data_access.IPresentationSpeakerDataStore;
import org.openstack.android.summit.modules.member_profile.IMemberProfileWireframe;
import org.openstack.android.summit.modules.speakers_list.ISpeakerListWireframe;
import org.openstack.android.summit.modules.speakers_list.SpeakerListWireframe;
import org.openstack.android.summit.modules.speakers_list.business_logic.ISpeakerListInteractor;
import org.openstack.android.summit.modules.speakers_list.business_logic.SpeakerListInteractor;
import org.openstack.android.summit.modules.speakers_list.user_interface.ISpeakerListPresenter;
import org.openstack.android.summit.modules.speakers_list.user_interface.SpeakerListFragment;
import org.openstack.android.summit.modules.speakers_list.user_interface.SpeakerListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
@Module
public class SpeakerListModule {
    @Provides
    SpeakerListFragment providesSpeakerListFragment() {
        return new SpeakerListFragment();
    }

    @Provides
    ISpeakerListWireframe providesSpeakerListWireframe(IMemberProfileWireframe memberProfileWireframe, INavigationParametersStore navigationParametersStore) {
        return new SpeakerListWireframe(memberProfileWireframe, navigationParametersStore);
    }

    @Provides
    ISpeakerListInteractor providesSpeakerListInteractor(IPresentationSpeakerDataStore presentationSpeakerDataStore, IDTOAssembler dtoAssembler) {
        return new SpeakerListInteractor(presentationSpeakerDataStore, dtoAssembler);
    }

    @Provides
    ISpeakerListPresenter providesSpeakerListPresenter(ISpeakerListInteractor speakerListInteractor, ISpeakerListWireframe speakerListWireframe) {
        return new SpeakerListPresenter(speakerListInteractor, speakerListWireframe);
    }
}