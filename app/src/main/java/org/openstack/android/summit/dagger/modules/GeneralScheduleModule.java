package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.general_schedule.GeneralScheduleWireframe;
import org.openstack.android.summit.modules.general_schedule.IGeneralScheduleWireframe;
import org.openstack.android.summit.modules.general_schedule.business_logic.GeneralScheduleInteractor;
import org.openstack.android.summit.modules.general_schedule.business_logic.IGeneralScheduleInteractor;
import org.openstack.android.summit.modules.general_schedule.user_interface.GeneralScheduleFragment;
import org.openstack.android.summit.modules.general_schedule.user_interface.GeneralSchedulePresenter;
import org.openstack.android.summit.modules.general_schedule.user_interface.IGeneralSchedulePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 12/21/2015.
 */
@Module
public class GeneralScheduleModule {

    @Provides
    GeneralScheduleFragment providesGeneralScheduleFragment() {
        return new GeneralScheduleFragment();
    }

    @Provides
    IGeneralScheduleWireframe providesGeneralScheduleWireframe(IEventDetailWireframe eventDetailWireframe) {
        return new GeneralScheduleWireframe(eventDetailWireframe);
    }

    @Provides
    IGeneralScheduleInteractor providesGeneralScheduleInteractor(ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, IDTOAssembler dtoAssembler) {
        return new GeneralScheduleInteractor(summitEventDataStore, summitDataStore, dtoAssembler);
    }

    @Provides
    IGeneralSchedulePresenter providesGeneralSchedulePresenter(IGeneralScheduleInteractor generalScheduleInteractor, IGeneralScheduleWireframe generalScheduleWireframe) {
        return new GeneralSchedulePresenter(generalScheduleInteractor, generalScheduleWireframe);
    }

}
