package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.business_logic.IScheduleableInteractor;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.IPresentationSpeakerDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.ScheduleItemViewBuilder;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.member_profile.IMemberProfileWireframe;
import org.openstack.android.summit.modules.search.ISearchWireframe;
import org.openstack.android.summit.modules.search.SearchWireframe;
import org.openstack.android.summit.modules.search.business_logic.ISearchInteractor;
import org.openstack.android.summit.modules.search.business_logic.SearchInteractor;
import org.openstack.android.summit.modules.search.user_interface.ISearchPresenter;
import org.openstack.android.summit.modules.search.user_interface.SearchFragment;
import org.openstack.android.summit.modules.search.user_interface.SearchPresenter;
import org.openstack.android.summit.modules.track_schedule.ITrackScheduleWireframe;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
@Module
public class SearchModule {
    @Provides
    SearchFragment providesSearchResultFragment() {
        return new SearchFragment();
    }

    @Provides
    ISearchWireframe providesSearchWireframe(IMemberProfileWireframe memberProfileWireframe, ITrackScheduleWireframe trackScheduleWireframe, IEventDetailWireframe eventDetailWireframe, INavigationParametersStore navigationParametersStore) {
        return new SearchWireframe(memberProfileWireframe, trackScheduleWireframe, eventDetailWireframe, navigationParametersStore);
    }

    @Provides
    ISearchInteractor providesSearchInteractor(IScheduleableInteractor scheduleableInteractor, ISummitEventDataStore summitEventDataStore, IGenericDataStore genericDataStore, IPresentationSpeakerDataStore presentationSpeakerDataStore, IDTOAssembler dtoAssembler) {
        return new SearchInteractor(scheduleableInteractor, summitEventDataStore, genericDataStore, presentationSpeakerDataStore, dtoAssembler);
    }

    @Provides
    ISearchPresenter providesSearchResultPresenter(ISearchInteractor searchInteractor, ISearchWireframe searchWireframe, IScheduleablePresenter scheduleablePresenter) {
        return new SearchPresenter(searchInteractor, searchWireframe, scheduleablePresenter, new ScheduleItemViewBuilder());
    }
}

