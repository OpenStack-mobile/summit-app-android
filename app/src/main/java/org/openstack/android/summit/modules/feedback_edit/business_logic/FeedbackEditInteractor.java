package org.openstack.android.summit.modules.feedback_edit.business_logic;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.FeedbackDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.IMemberDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.exceptions.ValidationException;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.utils.RealmFactory;
import java.util.Date;
import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 2/17/2016.
 */
public class FeedbackEditInteractor extends BaseInteractor implements IFeedbackEditInteractor {

    IMemberDataStore memberDataStore;
    ISummitEventDataStore summitEventDataStore;

    public FeedbackEditInteractor
    (
            IMemberDataStore memberDataStore,
            ISummitEventDataStore summitEventDataStore,
            ISecurityManager securityManager,
             IDTOAssembler dtoAssembler,
            ISummitDataStore summitDataStore,
            ISummitSelector summitSelector,
            IReachability reachability
    )
    {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore, reachability);

        this.memberDataStore      = memberDataStore;
        this.summitEventDataStore = summitEventDataStore;
        this.reachability         = reachability;
    }

    @Override
    public Observable<FeedbackDTO> saveFeedback(int eventId, int rate, String review) throws ValidationException {

        if(!securityManager.isLoggedIn()){
            throw new ValidationException("User is not logged!");
        }

        String error = validateFeedback(rate, review);
        if (error != null) {
            throw new ValidationException(error);
        }

        Member member           = securityManager.getCurrentMember();
        SummitEvent summitEvent = summitEventDataStore.getById(eventId);
        Feedback old_feedback   = member.getFeedback().where().equalTo("event.id", eventId).findFirst();
        Boolean new_feedback    = (old_feedback == null);

        Feedback feedback = new Feedback();
        feedback.setEvent(summitEvent);
        feedback.setOwner(member);
        feedback.setRate(rate);
        feedback.setReview(review);
        feedback.setDate(new Date());

        if (new_feedback)
            return memberDataStore.addFeedback(member, feedback).map(internalId -> {
                        Feedback f = RealmFactory.transaction(session ->
                                session.where(Feedback.class).equalTo("internalId", internalId).findFirst()
                        );
                        return dtoAssembler.createDTO(f, FeedbackDTO.class);
                    }
            );
        // update
        return memberDataStore.updateFeedback(member, feedback).map( success -> {
            if (success) {
                Feedback f = RealmFactory.transaction(session ->
                        session.where(Feedback.class).equalTo("event.id", eventId).findFirst());
                return dtoAssembler.createDTO(f, FeedbackDTO.class);
            }
            return null;
        });
    }

    private String validateFeedback(int rate, String review) {
        String errorMessage = null;
        if (rate == 0) {
            errorMessage = "You must provide a rate using stars at the top";
        }
        else if (review != null && review.length() > 500) {
            errorMessage = "Review exceeded 500 characters limit";
        }
        return errorMessage;
    }

    @Override
    public Feedback getFeedback(int eventId) throws ValidationException {
        if (!isNetworkingAvailable()) {
            throw new ValidationException(OpenStackSummitApplication.context.getResources().getString(R.string.no_connectivity_message));
        }

        if(!securityManager.isLoggedIn()){
            throw new ValidationException("User is not logged!");
        }

        Member member           = securityManager.getCurrentMember();
        return member.getFeedback().where().equalTo("event.id", eventId).findFirst();

    }

}
