package org.openstack.android.summit.modules.feedback_edit.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.exceptions.ValidationException;
import org.openstack.android.summit.common.user_interface.AlertsBuilder;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.feedback_edit.IFeedbackEditWireframe;
import org.openstack.android.summit.modules.feedback_edit.business_logic.IFeedbackEditInteractor;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Claudio Redi on 2/17/2016.
 */
public class FeedbackEditPresenter
        extends BasePresenter<IFeedbackEditView, IFeedbackEditInteractor, IFeedbackEditWireframe>
        implements IFeedbackEditPresenter {

    private Integer eventId;
    private String  eventName;
    private Integer rate;
    private String review;

    public FeedbackEditPresenter(IFeedbackEditInteractor interactor, IFeedbackEditWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);

        eventId = (savedInstanceState != null) ?
                savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_EVENT_ID, 0):
                wireframe.getParameter(Constants.NAVIGATION_PARAMETER_EVENT_ID, Integer.class);

        eventName = (savedInstanceState != null) ?
                savedInstanceState.getString(Constants.NAVIGATION_PARAMETER_EVENT_NAME, null):
                wireframe.getParameter(Constants.NAVIGATION_PARAMETER_EVENT_NAME, String.class);

        rate  = (savedInstanceState != null) ?
                savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_EVENT_RATE, 0):
                wireframe.getParameter(Constants.NAVIGATION_PARAMETER_EVENT_RATE, Integer.class);

        review = (savedInstanceState != null) ?
                savedInstanceState.getString(Constants.NAVIGATION_PARAMETER_EVENT_REVIEW, null):
                null;

        if(eventId != null && eventId > 0){
            try {
                Feedback feedback = interactor.getFeedback(eventId);
                if (feedback != null && rate == 0)
                    rate = feedback.getRate();

                if(feedback != null && review == null)
                    review = feedback.getReview();

            }
            catch (ValidationException ex){

            }
        }

        if(eventName != null && !eventName.trim().isEmpty()) {
            view.setEventName(eventName);
        }

        if(rate > 0)
            view.setRate(rate);

        if(review != null && !review.trim().isEmpty())
            view.setReview(review);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (eventId != null)
            outState.putInt(Constants.NAVIGATION_PARAMETER_EVENT_ID, eventId);

        rate = view.getRate();
        outState.putInt(Constants.NAVIGATION_PARAMETER_EVENT_RATE, rate);

        review = view.getReview();
        outState.putString(Constants.NAVIGATION_PARAMETER_EVENT_REVIEW, review);
    }

    @Override
    public void saveFeedback() {

        view.showActivityIndicator();

        try {

            interactor
                    .saveFeedback(eventId, view.getRate(), view.getReview())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (data) -> {
                                view.hideActivityIndicator();
                                wireframe.backToPreviousView(view);
                            },
                            (ex) -> {
                                view.hideActivityIndicator();
                                AlertsBuilder.buildGenericError(view.getFragmentActivity()).show();
                            }
                    );
        }
        catch (ValidationException ex){
            view.hideActivityIndicator();
            AlertsBuilder.buildValidationError(view.getFragmentActivity() ,ex.getMessage()).show();
        }
    }
}