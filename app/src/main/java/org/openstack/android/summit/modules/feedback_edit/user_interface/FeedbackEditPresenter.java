package org.openstack.android.summit.modules.feedback_edit.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.FeedbackDTO;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.business_logic.InteractorAsyncOperationListener;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.feedback_edit.IFeedbackEditWireframe;
import org.openstack.android.summit.modules.feedback_edit.business_logic.IFeedbackEditInteractor;

/**
 * Created by Claudio Redi on 2/17/2016.
 */
public class FeedbackEditPresenter extends BasePresenter<IFeedbackEditView, IFeedbackEditInteractor, IFeedbackEditWireframe> implements IFeedbackEditPresenter {
    private Integer eventId;
    private Integer rate;

    public FeedbackEditPresenter(IFeedbackEditInteractor interactor, IFeedbackEditWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventId = (savedInstanceState != null) ?
        savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_EVENT_ID, 0):
        wireframe.getParameter(Constants.NAVIGATION_PARAMETER_EVENT_ID, Integer.class);

        rate = (savedInstanceState != null) ?
                savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_EVENT_RATE, 0):
                wireframe.getParameter(Constants.NAVIGATION_PARAMETER_EVENT_RATE, Integer.class);

    }

    @Override
    public void onResume() {
        super.onResume();
        view.setRate(rate);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(eventId != null)
            outState.putInt(Constants.NAVIGATION_PARAMETER_EVENT_ID, eventId);

        if(rate != null)
            outState.putInt(Constants.NAVIGATION_PARAMETER_EVENT_RATE, eventId);
    }

    @Override
    public void saveFeedback() {
        view.showActivityIndicator();

        IInteractorAsyncOperationListener<FeedbackDTO> interactorAsyncOperationListener = new InteractorAsyncOperationListener<FeedbackDTO>() {
            @Override
            public void onSucceedWithData(FeedbackDTO data) {
                super.onSucceedWithData(data);
                view.hideActivityIndicator();
                wireframe.backToPreviousView(view);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                view.hideActivityIndicator();
                view.showErrorMessage(message);
            }
        };

        interactor.saveFeedback(view.getRate(), view.getReview(), eventId, interactorAsyncOperationListener);
    }
}