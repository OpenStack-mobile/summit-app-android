package org.openstack.android.summit.modules.feedback_edit;

import android.support.v4.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.event_detail.user_interface.EventDetailFragment;
import org.openstack.android.summit.modules.feedback_edit.user_interface.FeedbackEditFragment;

/**
 * Created by Claudio Redi on 2/17/2016.
 */
public class FeedbackEditWireframe extends BaseWireframe implements IFeedbackEditWireframe {
    public FeedbackEditWireframe(INavigationParametersStore navigationParametersStore) {
        super(navigationParametersStore);
    }

    @Override
    public void presentFeedbackEditView(int eventId, IBaseView context) {
        navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_EVENT_ID, eventId);
        FeedbackEditFragment feedbackEditFragment = new FeedbackEditFragment();
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, feedbackEditFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void backToPreviousView(IBaseView context) {
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager.popBackStack();
    }
}
