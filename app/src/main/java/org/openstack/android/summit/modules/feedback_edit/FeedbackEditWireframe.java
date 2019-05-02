package org.openstack.android.summit.modules.feedback_edit;

import androidx.fragment.app.FragmentManager;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.feedback_edit.user_interface.FeedbackEditFragment;

/**
 * Created by Claudio Redi on 2/17/2016.
 */
public class FeedbackEditWireframe extends BaseWireframe implements IFeedbackEditWireframe {
    public FeedbackEditWireframe(INavigationParametersStore navigationParametersStore) {
        super(navigationParametersStore);
    }

    @Override
    public void presentFeedbackEditView(int eventId,  String eventName, int rate, IBaseView context) {

        navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_EVENT_ID, eventId);
        navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_EVENT_NAME, eventName);
        navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_EVENT_RATE, rate);

        FeedbackEditFragment feedbackEditFragment = new FeedbackEditFragment();
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, feedbackEditFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    @Override
    public void backToPreviousView(IBaseView context) {
        try {
            FragmentManager fragmentManager = context.getSupportFragmentManager();
            fragmentManager.popBackStack();
        }
        catch(Exception ex){
            Crashlytics.logException(ex);
        }
    }
}
