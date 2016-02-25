package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.modules.feedback_given_list.business_logic.FeedbackGivenListInteractor;
import org.openstack.android.summit.modules.feedback_given_list.business_logic.IFeedbackGivenListInteractor;
import org.openstack.android.summit.modules.feedback_given_list.user_interface.FeedbackGivenListFragment;
import org.openstack.android.summit.modules.feedback_given_list.user_interface.FeedbackGivenListPresenter;
import org.openstack.android.summit.modules.feedback_given_list.user_interface.IFeedbackGivenListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
@Module
public class FeedbackGivenListModule {
    @Provides
    FeedbackGivenListFragment providesFeedbackGivenListFragment() {
        return new FeedbackGivenListFragment();
    }

    @Provides
    IFeedbackGivenListInteractor providesFeedbackGivenListInteractor(ISecurityManager securityManager, IDTOAssembler dtoAssembler, IDataUpdatePoller dataUpdatePoller) {
        return new FeedbackGivenListInteractor(securityManager, dtoAssembler, dataUpdatePoller);
    }

    @Provides
    IFeedbackGivenListPresenter providesFeedbackGivenListPresenter(IFeedbackGivenListInteractor interactor) {
        return new FeedbackGivenListPresenter(interactor);
    }
}
