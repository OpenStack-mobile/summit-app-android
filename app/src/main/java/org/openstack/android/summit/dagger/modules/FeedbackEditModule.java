package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.network.Reachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.modules.feedback_edit.FeedbackEditWireframe;
import org.openstack.android.summit.modules.feedback_edit.IFeedbackEditWireframe;
import org.openstack.android.summit.modules.feedback_edit.business_logic.FeedbackEditInteractor;
import org.openstack.android.summit.modules.feedback_edit.business_logic.IFeedbackEditInteractor;
import org.openstack.android.summit.modules.feedback_edit.user_interface.FeedbackEditFragment;
import org.openstack.android.summit.modules.feedback_edit.user_interface.FeedbackEditPresenter;
import org.openstack.android.summit.modules.feedback_edit.user_interface.IFeedbackEditPresenter;
import org.openstack.android.summit.modules.general_schedule_filter.IGeneralScheduleFilterWireframe;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 2/18/2016.
 */
@Module
public class FeedbackEditModule {
    @Provides
    FeedbackEditFragment providesFeedbackEditFragment() {
        return new FeedbackEditFragment();
    }

    @Provides
    IFeedbackEditWireframe providesFeedbackEditWireframe(INavigationParametersStore navigationParametersStore) {
        return new FeedbackEditWireframe(navigationParametersStore);
    }

    @Provides
    IFeedbackEditInteractor providesFeedbackEditInteractor(ISummitAttendeeDataStore summitAttendeeDataStore, IGenericDataStore genericDataStore, ISecurityManager securityManager, IDTOAssembler dtoAssembler, IDataUpdatePoller dataUpdatePoller) {
        return new FeedbackEditInteractor(summitAttendeeDataStore, genericDataStore, securityManager, new Reachability(), dtoAssembler, dataUpdatePoller);
    }

    @Provides
    IFeedbackEditPresenter providesFeedbackEditPresenter(IFeedbackEditInteractor feedbackEditInteractor, IFeedbackEditWireframe feedbackEditWireframe) {
        return new FeedbackEditPresenter(feedbackEditInteractor, feedbackEditWireframe);
    }    
}
